import { useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';
import { LoginForm } from '@/components/login/login-form';

const LoginPage = () => {
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (payload: { email: string; password: string }) => {
    setError('');
    setIsLoading(true);

    try {
      const response = await api.post('/auth/login', payload);

      if (response.data.status === 'ok') {
        localStorage.setItem('token', response.data.token);
        console.log('Login exitoso, token guardado');
        window.location.href = '/assignments'; 
      } else {
        setError('Email o contraseña incorrectos');
      }
    } catch (err: unknown) {
      console.error(err);
      setError('No se pudo conectar con el servidor de autenticación');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans p-4">
      
      {/* Tarjeta */}
      <div className="w-full max-w-md p-10 bg-white/40 backdrop-blur-xl border border-white/80 rounded-3xl shadow-xl shadow-neutral-200/40">
        
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold text-neutral-900 tracking-tight mb-1.5 italic">Innovatech</h1>
          <p className="text-neutral-500 text-[10px] font-bold uppercase tracking-[0.2em]">Auth Service</p>
        </div>

        {/* formulario */}
        <LoginForm onLogin={handleLogin} error={error} isLoading={isLoading} />

        <div className="mt-8 text-center">
          <p className="text-xs text-neutral-400">
            ¿No tienes acceso?{' '}
            <Link 
              to="/register" 
              className="text-neutral-600 cursor-pointer hover:text-neutral-900 font-medium transition-colors underline underline-offset-4 decoration-neutral-200 hover:decoration-neutral-400"
            >
              Regístrate aquí
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;