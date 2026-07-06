import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { EmployeeCard } from './employee-card';
import '@testing-library/jest-dom';
import React from 'react';

describe('EmployeeCard Component', () => {
  const mockEmployee = {
    id: 99,
    rut: '12.345.678-9',
    firstName: 'Juan',
    lastName: 'Pérez',
    email: 'juan.perez@empresa.com'
  };

  it('debería mostrar correctamente el nombre completo, RUT y correo del empleado', () => {
    const mockOnDelete = vi.fn();

    render(<EmployeeCard item={mockEmployee} onDelete={mockOnDelete} />);

    // Verificamos que concatene bien "Juan Pérez"
    expect(screen.getByText('Juan Pérez')).toBeInTheDocument();
    // Verificamos el RUT y el email
    expect(screen.getByText('12.345.678-9')).toBeInTheDocument();
    expect(screen.getByText('juan.perez@empresa.com')).toBeInTheDocument();
  });

  it('debería gatillar la función onDelete con el ID correcto', () => {
    const mockOnDelete = vi.fn();

    render(<EmployeeCard item={mockEmployee} onDelete={mockOnDelete} />);

    // Buscamos el botón de eliminar y hacemos click
    const botonEliminar = screen.getByRole('button');
    fireEvent.click(botonEliminar);

    // Verificamos que se llame con el ID correspondiente (99)
    expect(mockOnDelete).toHaveBeenCalledWith(99);
  });
});