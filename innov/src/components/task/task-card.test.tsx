// @vitest-environment jsdom
import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';

// Mockeamos solo los iconos visuales
vi.mock('lucide-react', () => ({
  Trash2: () => <span data-testid="icon-trash" />,
  Layers: () => <span data-testid="icon-layers" />,
  Calendar: () => <span data-testid="icon-calendar" />,
  Folder: () => <span data-testid="icon-folder" />
}));

import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { TaskCard } from './task-card';

describe('TaskCard Component', () => {
  const mockTask = {
    idTask: 55,
    name: 'Implementar Caché Redis',
    description: 'Añadir caché a las consultas principales para mejorar rendimiento.',
    dateCreated: '15-10-2023',
    projectId: 12,
    taskStatus: {
      idTaskStatus: 1,
      status: 'PENDIENTE'
    }
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar correctamente la información de la tarea', () => {
    render(<TaskCard item={mockTask} onDelete={vi.fn()} />);

    expect(screen.getByText('Implementar Caché Redis')).toBeInTheDocument();
    expect(screen.getByText('Añadir caché a las consultas principales para mejorar rendimiento.')).toBeInTheDocument();
    expect(screen.getByText('ID-55')).toBeInTheDocument();
    expect(screen.getByText('PRJ: 12')).toBeInTheDocument();
  });

  it('debería llamar a onDelete con el idTask correcto al hacer click en eliminar', () => {
    const mockOnDelete = vi.fn();
    render(<TaskCard item={mockTask} onDelete={mockOnDelete} />);

    const botonEliminar = screen.getByRole('button');
    fireEvent.click(botonEliminar);

    expect(mockOnDelete).toHaveBeenCalledWith(55);
  });
});