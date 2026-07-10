import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/api/axios';
import { ProjectCard } from '@/components/project/project-card';
import { Plus, Trash2 } from 'lucide-react'; 

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

  useEffect(() => {
    loadProjects();
  }, []);

  const handleSelectProject = (id: number) => {
    navigate(`/projects/${id}/dashboard`);
  };

  const handleDeleteProject = async (e: React.MouseEvent, id: number) => {
    e.stopPropagation(); 
    
    if (!window.confirm("¿Está seguro que desea eliminar este proyecto de Innovatech?")) {
      return;
    }

    try {
      const response = await api.delete(`/projects/${id}`);
      if (response.status === 200 || response.status === 204) {
        alert("¡Proyecto eliminado con éxito!");
        setProjects((prevProjects) => prevProjects.filter((p) => p.projectId !== id));
      }
    } catch (error) {
      console.error("Error al eliminar el proyecto", error);
      alert("Error al intentar eliminar el proyecto. Verifique las restricciones en el servidor.");
    }
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
      
      <header className="mb-10 max-w-7xl mx-auto flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div className="max-w-2xl">
          <span className="text-[10px] font-bold text-neutral-400 uppercase tracking-widest block mb-2">Módulo de Gestión</span>
          <h1 className="text-2xl lg:text-3xl font-extrabold text-neutral-800 tracking-tight">
            Proyectos
          </h1>
          <p className="text-sm text-neutral-600 mt-2">
            Seleccione un proyecto activo para auditar su ciclo operacional y consultar la agregación de tareas en tiempo real a través del BFF.
          </p>
        </div>

        <div className="flex-shrink-0">
          <button
            onClick={() => navigate('/projects/create')}
            className="inline-flex items-center gap-2 px-4 py-2.5 bg-neutral-900 text-white rounded-xl text-xs font-bold uppercase tracking-wider hover:bg-blue-600 transition-all active:scale-[0.98] shadow-sm shadow-neutral-900/10"
          >
            <Plus size={14} /> Nuevo Proyecto
          </button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map((project) => (
          <div key={project.projectId} className="relative group bg-white border border-neutral-200/60 rounded-2xl p-2 hover:shadow-md transition-all">
            <ProjectCard 
              project={project} 
              onSelect={handleSelectProject} 
            />
            
            <div className="absolute top-4 right-4 z-10">
              <button
                onClick={(e) => handleDeleteProject(e, project.projectId)}
                className="p-2 bg-neutral-50/90 backdrop-blur-sm border border-neutral-200 text-neutral-500 hover:text-red-600 hover:bg-red-50 hover:border-red-200 rounded-xl transition-all shadow-sm"
                title="Eliminar Proyecto"
              >
                <Trash2 size={14} />
              </button>
            </div>
          </div>
        ))}
      </main>
    </div>
  );
};