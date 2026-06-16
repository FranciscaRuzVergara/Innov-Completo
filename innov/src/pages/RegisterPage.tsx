import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ShieldCheck } from 'lucide-react';
import api from '@/api/axios';
import { RegisterForm } from '@/components/register/register-form';

export const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleRegister = async (payload: { email: string; password: string }) => {
    setError('');
    setIsLoading(true);

    try {
      const registerResponse = await api.post('/auth/register', payload);

      const message = registerResponse.data.message;

      if (message === "Usuario ya existe!") {
        setError("El correo electrónico ingresado ya se encuentra registrado.");
        setIsLoading(false);
        return;
      }

      const loginResponse = await api.post('/auth/login', payload);

      if (loginResponse.data.status === "ok" && loginResponse.data.token) {
        localStorage.setItem('token', loginResponse.data.token);
        navigate('/employees');
      } else {
        navigate('/login');
      }

    } catch (err) {
      console.error("Error en el flujo de registro:", err);
      setError("Ocurrió un error en el servidor de autenticación. Intente más tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans p-6">
      <div className="w-full max-w-md p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30">
        
        <div className="flex flex-col items-center mb-8">
          <span className="p-3 bg-neutral-900 text-white rounded-2xl shadow-sm mb-3">
            <ShieldCheck size={24} />
          </span>
          <h1 className="text-xl font-extrabold text-neutral-800 uppercase tracking-widest text-center">
            Crear Cuenta
          </h1>
          <p className="text-xs text-neutral-500 mt-1 text-center">
            Regístrese para acceder a la plataforma de gestión de Innovatech
          </p>
        </div>

        <RegisterForm onRegister={handleRegister} error={error} isLoading={isLoading} />

        {/* Enlace de Retorno */}
        <div className="mt-6 text-center border-t border-neutral-200/30 pt-4">
          <p className="text-xs text-neutral-500">
            ¿Ya tienes una cuenta activa?{' '}
            <Link to="/login" className="font-bold text-neutral-800 hover:underline">
              Inicia Sesión
            </Link>
          </p>
        </div>

      </div>
    </div>
  );
};