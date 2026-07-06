// @vitest-environment jsdom
import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { http, HttpResponse } from 'msw';

// Importamos el servidor usando tu alias o la ruta relativa exacta
import { server } from '@/mocks/server';

// Mockeamos Lucide para neutralizar el icono Plus y evitar que ensucie el DOM virtual
vi.mock('lucide-react', () => ({
  Plus: () => <span data-testid="icon-plus" />
}));

import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { TaskForm } from './task-form';

describe('TaskForm Component with MSW', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    
    // Espiamos e interceptamos el alert nativo para capturar los mensajes de error
    vi.spyOn(window, 'alert').mockImplementation(() => {});
  });

  it('debería inicializar los campos vacíos y renderizar las opciones de estado provistas por MSW', async () => {
    server.use(
      http.get('*/task-status/all', () => {
        return HttpResponse.json([
          { idTaskStatus: 10, status: 'BACKLOG' },
          { idTaskStatus: 20, status: 'DESARROLLO' }
        ]);
      })
    );

    render(<TaskForm onCreate={vi.fn()} />);

    expect(screen.getByPlaceholderText('Nombre de la tarea')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('ID de Proyecto Relacionado')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('BACKLOG')).toBeInTheDocument();
      expect(screen.getByText('DESARROLLO')).toBeInTheDocument();
    });
  });

  it('debería abortar el flujo y lanzar un alert de integridad si el proyecto no existe (MSW responde 404)', async () => {
    server.use(
      http.get('*/task-status/all', () => {
        return HttpResponse.json([{ idTaskStatus: 1, status: 'PENDIENTE' }]);
      }),
      // El asterisco intercepta cualquier URL base de Axios que termine en /projects/999
      http.get('*/projects/999', () => {
        return new HttpResponse(null, { status: 404 });
      })
    );

    const mockOnCreate = vi.fn();
    const { container } = render(<TaskForm onCreate={mockOnCreate} />);

    // Llenamos el formulario
    fireEvent.change(screen.getByPlaceholderText('Nombre de la tarea'), { target: { value: 'Tarea Invalida' } });
    fireEvent.change(screen.getByPlaceholderText('Descripción del entregable...'), { target: { value: 'Prueba de error' } });
    fireEvent.change(screen.getByPlaceholderText('ID de Proyecto Relacionado'), { target: { value: '999' } });

    // Disparamos el evento submit directamente en el elemento <form> para asegurar la ejecución asíncrona
    const form = container.querySelector('form');
    if (form) fireEvent.submit(form);

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith(
        expect.stringContaining('Error de Integridad: El ID de Proyecto [999] no existe')
      );
      expect(mockOnCreate).not.toHaveBeenCalled();
    });
  });

  it('debería procesar la creación de la tarea de forma exitosa y limpiar el formulario', async () => {
    // 1. Configuramos el backend falso de MSW
    server.use(
      http.get('*/task-status/all', () => {
        return HttpResponse.json([
          { idTaskStatus: 3, status: 'COMPLETADA' }
        ]);
      }),
      http.get('*/projects/77', () => {
        return HttpResponse.json({ id: 77, name: 'Proyecto Core' });
      })
    );

    const mockOnCreate = vi.fn().mockResolvedValue({});
    const { container } = render(<TaskForm onCreate={mockOnCreate} />);

    // 2. ¡EL TRUCO CLAVE! Esperamos a que el combobox de React se actualice con la data de MSW
    await waitFor(() => {
      expect(screen.getByText('COMPLETADA')).toBeInTheDocument();
    });

    // 3. Ahora que el estado interno de React está listo, llenamos los campos
    fireEvent.change(screen.getByPlaceholderText('Nombre de la tarea'), { target: { value: 'Refactorizar Servicios' } });
    fireEvent.change(screen.getByPlaceholderText('Descripción del entregable...'), { target: { value: 'Limpieza de código muerto' } });
    fireEvent.change(screen.getByPlaceholderText('ID de Proyecto Relacionado'), { target: { value: '77' } });

    // 4. Enviamos el formulario
    const form = container.querySelector('form');
    if (form) fireEvent.submit(form);

    // 5. Validamos que se haya enviado con el estado correcto (id: 3, COMPLETADA)
    await waitFor(() => {
      expect(mockOnCreate).toHaveBeenCalledWith(expect.objectContaining({
        name: 'Refactorizar Servicios',
        description: 'Limpieza de código muerto',
        projectId: 77,
        taskStatus: {
          idTaskStatus: 3,
          status: 'COMPLETADA'
        },
        dateCreated: expect.any(String)
      }));

      // Comprobamos la limpieza del formulario tras completarse con éxito
      expect(screen.getByPlaceholderText('Nombre de la tarea')).toHaveValue('');
    });
  });
});