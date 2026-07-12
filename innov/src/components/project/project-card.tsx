import React from 'react';
import { Folder, ArrowRight, Calendar } from 'lucide-react';

interface ProjectItem {
  projectId: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
}

interface ProjectCardProps {
  project: ProjectItem;
  onSelect: (id: number) => void;
}

export const ProjectCard: React.FC<ProjectCardProps> = ({ project, onSelect }) => {
  return (
    <div className="group relative p-8 bg-white/40 backdrop-blur-md rounded-3xl hover:bg-white/70 flex flex-col justify-between">
      <div>
        <div className="flex items-center gap-3 mb-4">
          <span className="p-2 bg-neutral-900/5 text-neutral-700 rounded-xl border border-neutral-900/5 group-hover:bg-blue-500/10 group-hover:text-blue-600 transition-colors">
            <Folder size={18} />
          </span>
          <span className="text-[10px] font-mono text-neutral-400 font-bold bg-neutral-900/5 px-2 py-0.5 rounded">
            PRJ-{project.projectId}
          </span>
        </div>

        <h2 className="text-base font-bold text-neutral-800 mb-2 group-hover:text-neutral-900 transition-colors">
          {project.name}
        </h2>
        <p className="text-xs text-neutral-600 leading-relaxed mb-6 line-clamp-2">
          {project.description}
        </p>
      </div>

      <div className="border-t border-neutral-200/30 pt-4 mt-auto flex items-center justify-between">
        <div className="flex items-center gap-1.5 text-neutral-400 text-[10px] font-medium">
          <Calendar size={12} />
          <span>Termina: {project.endDate}</span>
        </div>
        
        <button
          onClick={() => onSelect(project.projectId)}
          className="inline-flex items-center gap-1 px-3 py-1.5 bg-neutral-900 text-white rounded-xl text-[10px] font-bold uppercase tracking-wider hover:bg-blue-600 transition-all active:scale-[0.98] shadow-sm"
        >
          Ver Dashboard <ArrowRight size={12} />
        </button>
      </div>
    </div>
  );
};