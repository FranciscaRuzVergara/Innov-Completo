import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Folder, AlertCircle, ArrowLeft, Layers } from 'lucide-react';
import { getDashboardProject, type ProjectWithTasks } from '@/services/bff-service';
import { ProjectTaskCard } from '@/components/project-dashboard/project-task-card';

export const ProjectDashboardPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [projectData, setProjectData] = useState<ProjectWithTasks | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const fetchBffData = async () => {
      if (!id) return;
      setIsLoading(true);
      setError('');
      
      const data = await getDashboardProject(Number(id));
      if (data) {
        setProjectData(data);
      } else {
        setError('No se pudo recuperar la información consolidada del proyecto o el recurso no existe.');
      }
      setIsLoading(false);
    };

    fetchBffData();
  }, [id]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30">
        <p className="text-sm font-semibold tracking-widest uppercase text-neutral-500 animate-pulse">
          Orquestando datos desde BFF...
        </p>
      </div>
    );
  }

  if (error || !projectData) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center p-4 bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30">
        <div className="p-8 bg-red-500/10 border border-red-200 backdrop-blur-md rounded-3xl max-w-md text-center shadow-sm">
          <AlertCircle className="text-red-600 mx-auto mb-4" size={32} />
          <p className="text-sm font-semibold text-red-600">{error}</p>
          <Link to="/projects" className="mt-6 inline-flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-neutral-700 hover:text-neutral-900 transition-all">
            <ArrowLeft size={14} /> Volver a Proyectos
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="absolute inset-0 overflow-y-auto p-6 lg:p-12 bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans flex flex-col">
      
      <div className="max-w-7xl w-full mx-auto flex flex-col pb-12">
        <div className="mb-8">
          <Link 
            to="/projects" 
            className="inline-flex items-center gap-2 px-4 py-2 bg-white/40 backdrop-blur-sm border border-white/60 rounded-xl text-xs font-bold uppercase tracking-wider text-neutral-600 hover:text-neutral-900 hover:bg-white/80 transition-all shadow-sm"
          >
            <ArrowLeft size={14} /> Volver
          </Link>
        </div>

        <header className="mb-10 p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30 relative overflow-hidden flex-shrink-0">
          <div className="absolute top-0 right-0 p-8 opacity-5 text-neutral-900 pointer-events-none">
            <Folder size={120} />
          </div>
          
          <div className="flex items-center gap-3 mb-3">
            <span className="p-2 bg-blue-500/10 text-blue-600 rounded-xl border border-blue-200/20">
              <Folder size={20} />
            </span>
            <span className="text-[10px] font-bold text-blue-600 uppercase tracking-widest bg-blue-500/5 px-2.5 py-1 rounded-md border border-blue-500/10">
              ID Proyecto: {projectData.projectId}
            </span>
          </div>
          
          <h1 className="text-2xl lg:text-3xl font-extrabold text-neutral-800 tracking-tight mb-2">
            {projectData.name}
          </h1>
          <p className="text-sm text-neutral-600 max-w-2xl leading-relaxed mb-6">
            {projectData.description}
          </p>

          <div className="flex flex-wrap gap-6 text-xs font-medium text-neutral-500 border-t border-neutral-200/30 pt-4">
            <div>
              <span className="font-bold uppercase tracking-wider block text-[9px] text-neutral-400 mb-0.5">Fecha Inicio</span>
              <span className="font-mono text-neutral-700 text-sm">{projectData.startDate}</span>
            </div>
            <div>
              <span className="font-bold uppercase tracking-wider block text-[9px] text-neutral-400 mb-0.5">Fecha Término</span>
              <span className="font-mono text-neutral-700 text-sm">{projectData.endDate}</span>
            </div>
            <div className="ml-auto flex items-center gap-2 bg-neutral-900/5 px-4 py-2 rounded-xl border border-neutral-900/5">
              <Layers size={14} className="text-neutral-500" />
              <span className="text-neutral-700 font-bold">Total Tareas: {projectData.tasks.length}</span>
            </div>
          </div>
        </header>

        <main className="w-full flex-grow">
          <h2 className="text-lg font-bold text-neutral-800 mb-6 flex items-center gap-2 px-2">
            <Layers size={18} className="text-neutral-500" /> Tareas Vinculadas al Ciclo
          </h2>

          {projectData.tasks.length === 0 ? (
            <div className="p-12 text-center bg-white/20 backdrop-blur-md border border-dashed border-neutral-300 rounded-3xl">
              <p className="text-sm font-medium text-neutral-500">Este proyecto no cuenta con tareas asignadas en este sprint.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {projectData.tasks.map((task) => (
                <ProjectTaskCard key={task.taskId} task={task} />
              ))}
            </div>
          )}
        </main>
      </div>
    </div>
  );
};