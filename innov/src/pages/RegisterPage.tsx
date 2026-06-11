import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ShieldCheck, Mail, Lock, AlertCircle, UserPlus } from 'lucide-react';
import api from '@/api/axios'; // Tu cliente Axios conectado al Gateway

export const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      // 1. Primer paso: Registrar el usuario en el backend de Auth a través del Gateway
      const registerResponse = await api.post('/auth/register', {
        email: formData.email,
        password: formData.password
      });

      const message = registerResponse.data.message;

      // Tu backend devuelve "Usuario ya existe!" en texto plano si colisiona el correo
      if (message === "Usuario ya existe!") {
        setError("El correo electrónico ingresado ya se encuentra registrado.");
        setIsLoading(false);
        return;
      }

      // 2. Segundo paso: Login transparente para obtener el token JWT de inmediato
      const loginResponse = await api.post('/auth/login', {
        email: formData.email,
        password: formData.password
      });

      if (loginResponse.data.status === "ok" && loginResponse.data.token) {
        localStorage.setItem('token', loginResponse.data.token); // Guardamos el Bearer token
        navigate('/projects'); // Redirección directa al Portafolio
      } else {
        // Resguardo si el login falla: redirigir a que inicie sesión manualmente
        navigate('/login');
      }

    } catch (err) {
      console.error("🔥 Error en el flujo de registro:", err);
      setError("Ocurrió un error en el servidor de autenticación. Intente más tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans p-6">
      <div className="w-full max-w-md p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30">
        
        {/* Cabecera / Marca */}
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

        {/* Banner de Errores */}
        {error && (
          <div className="mb-6 p-4 bg-red-500/10 border border-red-200 text-red-600 rounded-xl flex items-start gap-2.5 text-xs font-medium">
            <AlertCircle size={16} className="mt-0.5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Formulario de Captura */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-[10px] font-bold text-neutral-400 uppercase tracking-wider mb-1.5 ml-1">
              Correo Electrónico
            </label>
            <div className="relative">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3.5 text-neutral-400 pointer-events-none">
                <Mail size={16} />
              </span>
              <input
                type="email"
                required
                placeholder="ejemplo@innovatech.com"
                className="w-full pl-10 pr-4 py-3 bg-white/50 backdrop-blur-sm border border-white/80 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              />
            </div>
          </div>

          <div>
            <label className="block text-[10px] font-bold text-neutral-400 uppercase tracking-wider mb-1.5 ml-1">
              Contraseña
            </label>
            <div className="relative">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3.5 text-neutral-400 pointer-events-none">
                <Lock size={16} />
              </span>
              <input
                type="password"
                required
                placeholder="••••••••"
                className="w-full pl-10 pr-4 py-3 bg-white/50 backdrop-blur-sm border border-white/80 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full mt-2 bg-neutral-900/90 hover:bg-neutral-900 text-white py-3 rounded-xl font-bold uppercase tracking-widest text-xs transition-all active:scale-[0.99] shadow-sm hover:shadow-md flex items-center justify-center gap-2 disabled:opacity-50 disabled:pointer-events-none"
          >
            <UserPlus size={14} />
            {isLoading ? 'Procesando Alta...' : 'Registrarse'}
          </button>
        </form>

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