import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/api/axios';
import { ProjectCard } from '@/components/project/project-card';


interface ProjectItem {
  projectId: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
}

export const ProjectsPage: React.FC = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState<ProjectItem[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const loadProjects = async () => {
      try {
        setIsLoading(true);
        const response = await api.get<ProjectItem[]>('/projects');
        if (response.status === 200) {
          setProjects(response.data);
        }
      } catch (error) {
        console.error("Error al cargar proyectos", error);
        alert("Error al cargar proyectos. Intente nuevamente más tarde.");
      } finally {
        setIsLoading(false);
      }
    };

    loadProjects();
  }, []);

  const handleSelectProject = (id: number) => {
    navigate(`/projects/${id}/dashboard`);
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30">
        <p className="text-sm font-semibold tracking-widest uppercase text-neutral-500 animate-pulse">
          Cargando catálogo de proyectos...
        </p>
      </div>
    );
  }

  return (
    <div className="min-h-screen p-6 lg:p-12 bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans">
      <header className="mb-10 max-w-2xl">
        <span className="text-[10px] font-bold text-neutral-400 uppercase tracking-widest block mb-2">Módulo de Gestión</span>
        <h1 className="text-2xl lg:text-3xl font-extrabold text-neutral-800 tracking-tight">
          Proyectos
        </h1>
        <p className="text-sm text-neutral-600 mt-2">
          Seleccione un proyecto activo para auditar su ciclo operacional y consultar la agregación de tareas en tiempo real a través del BFF.
        </p>
      </header>

      <main className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map((project) => (
          <ProjectCard 
            key={project.projectId} 
            project={project} 
            onSelect={handleSelectProject} 
          />
        ))}
      </main>
    </div>
  );
};