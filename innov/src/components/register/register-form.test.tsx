// @vitest-environment jsdom
import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';

// 1. Mockeamos la función de validación de correo electrónico
vi.mock('@/utils/validator-email', () => ({
  validateEmail: (email: string) => {
    // Si contiene la palabra 'invalido', simulamos que falla
    if (email.includes('invalido')) return false;
    // Si no la contiene, da true
    return true;
  }
}));

// Mockeamos la librería de iconos Lucide para que no ensucie el DOM virtual
vi.mock('lucide-react', () => ({
  Mail: () => <span data-testid="icon-mail" />,
  Lock: () => <span data-testid="icon-lock" />,
  UserPlus: () => <span data-testid="icon-userplus" />
}));

import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { RegisterForm } from './register-form';

describe('RegisterForm Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar correctamente todos los inputs, etiquetas y el botón de registro', () => {
    render(<RegisterForm onRegister={vi.fn()} error="" isLoading={false} />);

    expect(screen.getByText('Correo Electrónico')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('ejemplo@innovatech.com')).toBeInTheDocument();
    expect(screen.getByText('Contraseña')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('••••••••')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Registrarse/i })).toBeInTheDocument();
  });

  it('debería mostrar un error local si el formato del correo es inválido', () => {
    render(<RegisterForm onRegister={vi.fn()} error="" isLoading={false} />);

    // Rellenamos el email usando el truco para engañar a HTML5 (lleva @ pero tiene la palabra clave inválido para nuestro mock)
    fireEvent.change(screen.getByPlaceholderText('ejemplo@innovatech.com'), {
      target: { value: 'test@invalido' }
    });

    // Rellenamos la contraseña para cumplir el 'required' nativo
    fireEvent.change(screen.getByPlaceholderText('••••••••'), {
      target: { value: 'password123' }
    });

    // Enviamos el formulario
    fireEvent.click(screen.getByRole('button', { name: /Registrarse/i }));

    // Verificamos el banner de error local
    expect(
      screen.getByText('El formato del correo electrónico no es válido o contiene caracteres no permitidos.')
    ).toBeInTheDocument();
  });

  it('debería bloquear el submit y mostrar error local si la contraseña tiene un patrón anti-scripts', () => {
    const mockOnRegister = vi.fn();
    render(<RegisterForm onRegister={mockOnRegister} error="" isLoading={false} />);

    // Email correcto
    fireEvent.change(screen.getByPlaceholderText('ejemplo@innovatech.com'), {
      target: { value: 'usuario.valido@innovatech.com' }
    });

    // Contraseña maliciosa con inyección de etiquetas <script>
    fireEvent.change(screen.getByPlaceholderText('••••••••'), {
      target: { value: '<script>alert("hack")</script>' }
    });

    fireEvent.click(screen.getByRole('button', { name: /Registrarse/i }));

    // Verificamos la alerta de seguridad anti-scripts
    expect(
      screen.getByText('La contraseña contiene caracteres o estructuras de código no autorizadas.')
    ).toBeInTheDocument();
    expect(mockOnRegister).not.toHaveBeenCalled();
  });

  it('debería mostrar el error externo proveniente de las props del servidor si existe', () => {
    // Simulamos un error que nos devuelve el backend (ej: El correo ya está registrado)
    render(<RegisterForm onRegister={vi.fn()} error="El correo electrónico ya se encuentra registrado." isLoading={false} />);

    expect(screen.getByText('El correo electrónico ya se encuentra registrado.')).toBeInTheDocument();
  });

  it('debería deshabilitar el botón y mostrar el estado de carga si isLoading es verdadero', () => {
    render(<RegisterForm onRegister={vi.fn()} error="" isLoading={true} />);

    const submitBtn = screen.getByRole('button', { name: /Procesando Alta.../i });
    expect(submitBtn).toBeInTheDocument();
    expect(submitBtn).toBeDisabled();
  });

  it('debería llamar a onRegister con las credenciales correctas en un flujo exitoso', () => {
    const mockOnRegister = vi.fn().mockResolvedValue({});
    render(<RegisterForm onRegister={mockOnRegister} error="" isLoading={false} />);

    // Llenamos datos limpios y válidos
    fireEvent.change(screen.getByPlaceholderText('ejemplo@innovatech.com'), {
      target: { value: 'nuevo.usuario@innovatech.com' }
    });
    fireEvent.change(screen.getByPlaceholderText('••••••••'), {
      target: { value: 'SeguraYValida2026' }
    });

    // Enviamos
    fireEvent.click(screen.getByRole('button', { name: /Registrarse/i }));

    // Comprobamos que gatille la función con la estructura exacta que pide tu interfaz
    expect(mockOnRegister).toHaveBeenCalledWith({
      email: 'nuevo.usuario@innovatech.com',
      password: 'SeguraYValida2026'
    });
  });
});