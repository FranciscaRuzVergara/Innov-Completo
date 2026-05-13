import React, { useState } from 'react';
import api from '../api/axios';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await api.post('/auth/login', {
        email: email,
        password: password
      });

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
    <div className="relative min-h-screen w-full flex items-center justify-center bg-slate-950 bg-[url('https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&q=80&w=2070')] bg-cover bg-center bg-no-repeat font-sans overflow-hidden">
      
      {/* Capa de oscuridad sobre la imagen */}
      <div className="absolute inset-0 bg-gradient-to-br from-black/90 via-slate-900/80 to-black/90"></div>

      {/* Tarjeta Glassmorphism */}
      <div className="relative z-10 w-full max-w-md p-10 mx-4 bg-white/[0.05] backdrop-blur-2xl border border-white/10 rounded-[2.5rem] shadow-2xl">
        
        <div className="text-center mb-10">
          <h1 className="text-4xl font-bold text-white tracking-tighter mb-2 italic">Innovatech</h1>
          <p className="text-blue-400/50 text-xs font-bold uppercase tracking-[0.2em]">Auth Service</p>
        </div>

        <form className="space-y-6" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-xs p-3 rounded-xl text-center font-medium animate-pulse">
              {error}
            </div>
          )}

          <div>
            <label className="block text-white/50 text-[10px] font-bold uppercase tracking-widest mb-2 ml-1">Email</label>
            <input 
              type="email" 
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="usuario@innovatech.com"
              className="w-full px-5 py-4 bg-white/5 border border-white/10 rounded-2xl text-white placeholder:text-white/20 focus:outline-none focus:ring-1 focus:ring-blue-500/50 focus:bg-white/[0.08] transition-all outline-none"
            />
          </div>

          <div>
            <label className="block text-white/50 text-[10px] font-bold uppercase tracking-widest mb-2 ml-1">Password</label>
            <input 
              type="password" 
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              className="w-full px-5 py-4 bg-white/5 border border-white/10 rounded-2xl text-white placeholder:text-white/20 focus:outline-none focus:ring-1 focus:ring-blue-500/50 focus:bg-white/[0.08] transition-all outline-none"
            />
          </div>

          <button 
            type="submit"
            disabled={isLoading}
            className={`w-full py-4 px-4 font-black rounded-2xl transition-all active:scale-[0.98] mt-4 uppercase tracking-widest text-xs shadow-2xl ${
              isLoading ? 'bg-slate-800 text-slate-500' : 'bg-blue-600 hover:bg-blue-500 text-white shadow-blue-900/20'
            }`}
          >
            {isLoading ? 'Autenticando...' : 'Iniciar Sesión'}
          </button>
        </form>

        <div className="mt-10 text-center">
          <p className="text-xs text-white/30">
            ¿No tienes acceso? <span className="text-white/60 cursor-pointer hover:text-white transition-colors">Contactar soporte</span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;