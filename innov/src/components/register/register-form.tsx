import React, { useState } from 'react';
import { Mail, Lock, UserPlus } from 'lucide-react';
import { validateEmail } from '@/utils/validator-email';

interface RegisterFormProps {
  onRegister: (credentials: { email: string; password: string }) => Promise<void>;
  error: string;
  isLoading: boolean;
}

export const RegisterForm: React.FC<RegisterFormProps> = ({ onRegister, error, isLoading }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [localError, setLocalError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLocalError('');

    // Validación del formato de correo idéntica al Login
    if (!validateEmail(email)) {
      setLocalError('El formato del correo electrónico no es válido o contiene caracteres no permitidos.');
      return;
    }

    // Patrón de seguridad anti-scripts para la contraseña
    const scriptPattern = /<script\b[^>]*>([\s\S]*?)<\/script>|[<>]/gi;
    if (scriptPattern.test(password)) {
      setLocalError('La contraseña contiene caracteres o estructuras de código no autorizadas.');
      return;
    }

    onRegister({ email, password });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {/* Banner de Errores Unificado */}
      {(error || localError) && (
        <div className="mb-6 p-4 bg-red-500/10 border border-red-200 text-red-600 rounded-xl flex items-start gap-2.5 text-xs font-medium backdrop-blur-md justify-center">
          <span>{localError || error}</span>
        </div>
      )}

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
            value={email}
            onChange={(e) => setEmail(e.target.value)}
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
            value={password}
            onChange={(e) => setPassword(e.target.value)}
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
  );
};