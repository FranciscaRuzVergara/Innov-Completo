// @vitest-environment jsdom
import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';

// Mockeamos Lucide para aislar el renderizado de los SVG y evitar nodos pesados
vi.mock('lucide-react', () => ({
  Folder: () => <span data-testid="icon-folder" />,
  Calendar: () => <span data-testid="icon-calendar" />,
  ArrowRight: () => <span data-testid="icon-arrow" />
}));

import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { ProjectCard } from './project-card';

describe('ProjectCard Component', () => {
  const mockProject = {
    projectId: 104,
    name: 'Sistema de Gestión Innovatech',
    description: 'Desarrollo de un ecosistema modular para la administración de microservicios internos.',
    startDate: '01-01-2026',
    endDate: '31-12-2026'
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar adecuadamente toda la información del proyecto', () => {
    render(<ProjectCard project={mockProject} onSelect={vi.fn()} />);

    // Verificamos los textos principales
    expect(screen.getByText('Sistema de Gestión Innovatech')).toBeInTheDocument();
    expect(screen.getByText('Desarrollo de un ecosistema modular para la administración de microservicios internos.')).toBeInTheDocument();
    
    // Verificamos las etiquetas formateadas de ID y fecha de finalización
    expect(screen.getByText('PRJ-104')).toBeInTheDocument();
    expect(screen.getByText('Termina: 31-12-2026')).toBeInTheDocument();
  });

  it('debería ejecutar el callback onSelect con el projectId correcto al accionar el botón', () => {
    const mockOnSelect = vi.fn();
    render(<ProjectCard project={mockProject} onSelect={mockOnSelect} />);

    // Localizamos el botón interactivo de la tarjeta
    const botonDashboard = screen.getByRole('button', { name: /Ver Dashboard/i });
    expect(botonDashboard).toBeInTheDocument();

    // Simulamos el click
    fireEvent.click(botonDashboard);

    // Comprobamos que pasara exactamente el ID 104 del proyecto
    expect(mockOnSelect).toHaveBeenCalledTimes(1);
    expect(mockOnSelect).toHaveBeenCalledWith(104);
  });
});