// @vitest-environment jsdom
import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';

// 1. Mockeamos la ruta relativa exacta antes de importar el componente para neutralizar el alias @/
vi.mock('../../utils/validator-email', () => ({
  validateEmail: (email: string) => email.includes('@') && email.endsWith('.com'),
}));

// 2. Ahora que el entorno está a salvo, importamos las librerías de test y el componente
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { LoginForm } from './login-form';

describe('LoginForm Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería llamar a onLogin con las credenciales correctas al enviar un formulario válido', () => {
    const mockOnLogin = vi.fn();
    render(<LoginForm onLogin={mockOnLogin} error="" isLoading={false} />);

    // Buscamos los inputs por sus placeholders reales
    const emailInput = screen.getByPlaceholderText('usuario@innovatech.com');
    fireEvent.change(emailInput, { target: { value: 'test@innovatech.com' } });

    const passwordInput = screen.getByPlaceholderText('••••••••');
    fireEvent.change(passwordInput, { target: { value: 'passwordSeguro123' } });

    // Hacemos submit clickeando el botón
    const submitBtn = screen.getByRole('button', { name: /Iniciar Sesión/i });
    fireEvent.click(submitBtn);

    expect(mockOnLogin).toHaveBeenCalledWith({
      email: 'test@innovatech.com',
      password: 'passwordSeguro123',
    });
  });

 it('debería mostrar un error local si el formato del correo es inválido', () => {
    const mockOnLogin = vi.fn();
    render(<LoginForm onLogin={mockOnLogin} error="" isLoading={false} />);

    // 1. ¡EL TRUCO CLAVE! Usamos un formato que HTML5 acepte (lleva @) pero que tu validador rechace (no termina en .com)
    const emailInput = screen.getByPlaceholderText('usuario@innovatech.com');
    fireEvent.change(emailInput, { target: { value: 'test@invalido' } }); 

    // 2. Rellenamos la contraseña para cumplir el 'required'
    const passwordInput = screen.getByPlaceholderText('••••••••');
    fireEvent.change(passwordInput, { target: { value: 'passwordCualquiera123' } });

    // 3. Enviamos el formulario
    const submitBtn = screen.getByRole('button', { name: /Iniciar Sesión/i });
    fireEvent.click(submitBtn);

    // Ahora sí pasará los filtros nativos de HTML5, entrará a tu código y pintará el error
    expect(screen.getByText(/El formato del correo electrónico no es válido/i)).toBeInTheDocument();
    expect(mockOnLogin).not.toHaveBeenCalled(); 
  });
  it('debería bloquear el intento si detecta caracteres de script peligrosos en la contraseña', () => {
    const mockOnLogin = vi.fn();
    render(<LoginForm onLogin={mockOnLogin} error="" isLoading={false} />);

    const emailInput = screen.getByPlaceholderText('usuario@innovatech.com');
    fireEvent.change(emailInput, { target: { value: 'admin@innovatech.com' } });

    const passwordInput = screen.getByPlaceholderText('••••••••');
    fireEvent.change(passwordInput, { target: { value: '<script>alert("hack")</script>' } }); // Expresión regular lo detecta

    const submitBtn = screen.getByRole('button', { name: /Iniciar Sesión/i });
    fireEvent.click(submitBtn);

    // Validamos el error de script malicioso
    expect(screen.getByText(/La contraseña contiene caracteres o estructuras de código no autorizadas/i)).toBeInTheDocument();
    expect(mockOnLogin).not.toHaveBeenCalled();
  });
});