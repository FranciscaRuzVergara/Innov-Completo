import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { Navbar } from './navbar';
import React from 'react';
import '@testing-library/jest-dom/vitest';

// 1. MOCKEAMOS COMPLETAMENTE react-router-dom para evitar sus hooks reales
const mockNavigate = vi.fn();
let mockPathname = '/'; // Variable controlable para simular la ruta actual

vi.mock('react-router-dom', () => ({
  // Convertimos el componente <Link> en una etiqueta <a> común y corriente para el test
  Link: ({ to, children, className }: any) => (
    <a href={to} className={className}>
      {children}
    </a>
  ),
  useLocation: () => ({ pathname: mockPathname }),
  useNavigate: () => mockNavigate,
}));

// 2. Simulamos los iconos de lucide igual que en los tests anteriores
vi.mock('lucide-react', () => ({
  Users: () => <span>Users</span>,
  ClipboardList: () => <span>ClipboardList</span>,
  FolderKanban: () => <span>FolderKanban</span>,
  LogOut: () => <span>LogOut</span>,
  ShieldCheck: () => <span>ShieldCheck</span>,
}));

describe('Navbar Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
    mockPathname = '/'; // Reiniciamos la ruta por defecto antes de cada test
  });

  it('debería renderizar los enlaces de navegación principales', () => {
    // ¡Ya NO envolvemos en <BrowserRouter>! Renderiza limpio e inmune
    render(<Navbar />);

    expect(screen.getByText('Empleados')).toBeInTheDocument();
    expect(screen.getByText('Asignaciones')).toBeInTheDocument();
    expect(screen.getByText('Proyectos')).toBeInTheDocument();
    expect(screen.getByText('Tareas')).toBeInTheDocument();
  });

  it('debería resaltar el enlace activo según la ruta de useLocation', () => {
    mockPathname = '/projects'; // Simulamos que estamos en la página de proyectos
    render(<Navbar />);

    const linkProyectos = screen.getByText('Proyectos').closest('a');
    // Verificamos que se le aplique la clase de diseño activo que definió tu compañero
    expect(linkProyectos).toHaveClass('bg-neutral-900');
  });

  it('debería borrar el token del localStorage y redirigir al login al hacer click en cerrar sesión', () => {
    localStorage.setItem('token', 'mi-token-falso');

    render(<Navbar />);

    // Buscamos el botón de cerrar sesión
    const botonLogout = screen.getByRole('button', { name: /salir/i });
    fireEvent.click(botonLogout);

    // Verificaciones críticas de comportamiento
    expect(localStorage.getItem('token')).toBeNull(); // El token fue eliminado
    expect(mockNavigate).toHaveBeenCalledWith('/login'); // Se llamó a la redirección
  });
});