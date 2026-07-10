import React from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { ProjectForm } from "@/components/project/project-form";

export const CreateProjectPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen p-6 lg:p-12 bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans">
      <button
        onClick={() => navigate("/projects")}
        className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-neutral-500 hover:text-neutral-800 mb-6 transition-colors"
      >
        <ArrowLeft size={14} /> Volver al catálogo
      </button>

      <header className="mb-10 max-w-2xl">
        <h1 className="text-2xl lg:text-3xl font-extrabold text-neutral-800 tracking-tight">
          Crear Nuevo Proyecto
        </h1>
        <p className="text-sm text-neutral-600 mt-2">
          Ingrese los parámetros del dominio para forzar la inserción relacional
          en cascada dentro de MySQL.
        </p>
      </header>

      <main className="max-w-xl bg-white/40 backdrop-blur-md border border-white/70 p-8 rounded-3xl shadow-sm">
        <ProjectForm />
      </main>
    </div>
  );
};
