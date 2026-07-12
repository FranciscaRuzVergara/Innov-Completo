import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { ProjectForm } from '@/components/project/project-form';

export const CreateProjectPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    // MODIFICADO: Cambiamos a absolute inset-0 y overflow-y-auto combinados con py-12 para garantizar el scroll vertical hacia abajo
    <div className="absolute inset-0 overflow-y-auto bg-neutral-50 font-sans flex justify-center items-start py-12 md:py-20 px-4 relative selection:bg-blue-500/10">
      
      {/* ORBES DE LUZ SUTILES: Mantienen el efecto de profundidad moderno vistiendo el fondo */}
      <div className="absolute top-[-10%] left-[-10%] w-[50vw] h-[50vw] rounded-full bg-blue-400/10 blur-[120px] pointer-events-none animate-pulse" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[45vw] h-[45vw] rounded-full bg-indigo-500/10 blur-[140px] pointer-events-none" />

      {/* CONTENEDOR CENTRAL: Centrado de forma elástica, permitiendo el crecimiento natural del main */}
      <div className="max-w-xl w-full flex flex-col relative z-10 my-auto">
        
        {/* Acción de navegación superior limpia */}
        <div className="mb-4">
          <button 
            onClick={() => navigate('/projects')}
            className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-neutral-400 hover:text-neutral-800 transition-colors"
          >
            <ArrowLeft size={14} /> Volver al catálogo
          </button>
        </div>

        {/* Encabezado expandido simétrico */}
        <header className="mb-8 w-full border-b border-neutral-200/50 pb-6">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-[9px] font-bold text-neutral-500 uppercase tracking-widest bg-neutral-200/60 px-2 py-0.5 rounded font-mono">
              SISTEMA CORE
            </span>
          </div>
          <h1 className="text-2xl lg:text-3xl font-black text-neutral-900 tracking-tight">
            Crear Nuevo Proyecto
          </h1>
          <p className="text-xs text-neutral-500 mt-2 leading-relaxed">
            Ingrese los parámetros del dominio para forzar la inserción relacional en cascada dentro de MySQL de manera atómica.
          </p>
        </header>

        {/* TARJETA PREMIUM (MAIN): Encapsula el formulario con un diseño de cristal opaco suavizado */}
        <main className="w-full bg-white border border-neutral-200/80 p-6 md:p-10 rounded-[24px] shadow-xl shadow-neutral-200/30">
          <ProjectForm />
        </main>

        {/* Footer minimalista discreto de cierre */}
        <footer className="mt-8 text-center text-[10px] text-neutral-400 font-medium tracking-wide">
          Ecosistema Innovatech • Gestión Distribuidora de Microservicios
        </footer>

      </div>
    </div>
  );
};