
import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { AssignmentCard } from './assignment-card';
import '@testing-library/jest-dom';
import React from 'react';

vi.mock('lucide-react', () => ({
  Trash2: () => <span data-testid="icon-trash" />,
  Clock: () => <span />,
  User: () => <span />,
  Briefcase: () => <span />,
}));

describe('AssignmentCard Component', () => {
  const mockAssignment = {
    id: 123,
    employeeRut: '12.345.678-9',
    taskRoleId: 5,
    assignedHours: 45
  };

  it('debería renderizar correctamente los datos de la asignación', () => {
    const mockOnDelete = vi.fn();
    render(<AssignmentCard item={mockAssignment} onDelete={mockOnDelete} />);

    expect(screen.getByText('12.345.678-9')).toBeInTheDocument();
    expect(screen.getByText('#5')).toBeInTheDocument();
    expect(screen.getByText('45 hrs')).toBeInTheDocument();
  });

  it('debería llamar a la función onDelete con el ID correcto al hacer click', () => {
    const mockOnDelete = vi.fn();
    render(<AssignmentCard item={mockAssignment} onDelete={mockOnDelete} />);

    const botonEliminar = screen.getByRole('button');
    fireEvent.click(botonEliminar);

    expect(mockOnDelete).toHaveBeenCalledWith(123);
  });
});