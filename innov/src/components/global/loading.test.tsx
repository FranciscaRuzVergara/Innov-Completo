import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { GlobalLoading } from './loading';
import React from 'react';

describe('GlobalLoading Component', () => {
  it('debería mostrar el mensaje de carga por defecto', () => {
    render(<GlobalLoading />);
    expect(screen.getByText('Cargando información consolidada...')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje personalizado si se pasa por prop', () => {
    render(<GlobalLoading message="Buscando datos del microservicio..." />);
    expect(screen.getByText('Buscando datos del microservicio...')).toBeInTheDocument();
  });
});
