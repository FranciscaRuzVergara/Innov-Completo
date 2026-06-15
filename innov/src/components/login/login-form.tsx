import React, { useState } from 'react';
import { validateEmail } from '@/utils/validator-email';

interface LoginFormProps {
  onLogin: (credentials: { email: string; password: string }) => Promise<void>;
  error: string;
  isLoading: boolean;
}

export const LoginForm: React.FC<LoginFormProps> = ({ onLogin, error, isLoading }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [localError, setLocalError] = useState(''); 

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLocalError('');

    if (!validateEmail(email)) {
      setLocalError('El formato del correo electrónico no es válido o contiene caracteres no permitidos.');
      return;
    }

    const scriptPattern = /<script\b[^>]*>([\s\S]*?)<\/script>|[<>]/gi;
    if (scriptPattern.test(password)) {
      setLocalError('La contraseña contiene caracteres o estructuras de código no autorizadas.');
      return;
    }

    onLogin({ email, password });
  };

  return (
    <form className="space-y-5" onSubmit={handleSubmit}>
      {(error || localError) && (
        <div className="bg-red-500/10 border border-red-200 text-red-600 text-xs p-3.5 rounded-xl text-center font-semibold tracking-wide backdrop-blur-md">
          {localError || error}
        </div>
      )}

      <div>
        <label className="block text-neutral-600 text-[10px] font-bold uppercase tracking-wider mb-1.5 ml-1">Email</label>
        <input 
          type="email" 
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="usuario@innovatech.com"
          className="w-full px-4 py-3.5 bg-white/40 backdrop-blur-md border border-white/60 rounded-xl text-neutral-800 placeholder:text-neutral-400 focus:outline-none focus:border-blue-500/60 focus:bg-white/80 transition-all outline-none text-sm shadow-sm"
        />
      </div>

      <div>
        <label className="block text-neutral-600 text-[10px] font-bold uppercase tracking-wider mb-1.5 ml-1">Password</label>
        <input 
          type="password" 
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="••••••••"
          className="w-full px-4 py-3.5 bg-white/40 backdrop-blur-md border border-white/60 rounded-xl text-neutral-800 placeholder:text-neutral-400 focus:outline-none focus:border-blue-500/60 focus:bg-white/80 transition-all outline-none text-sm shadow-sm"
        />
      </div>

      <button 
        type="submit"
        disabled={isLoading}
        className={`w-full py-3.5 px-4 font-bold rounded-xl transition-all active:scale-[0.99] mt-2 uppercase tracking-widest text-xs border backdrop-blur-md ${
          isLoading 
            ? 'bg-white/20 border-white/40 text-neutral-400 cursor-not-allowed' 
            : 'bg-neutral-900/90 hover:bg-neutral-900 text-white border-transparent shadow-sm hover:shadow-md'
        }`}
      >
        {isLoading ? 'Autenticando...' : 'Iniciar Sesión'}
      </button>
    </form>
  );
};