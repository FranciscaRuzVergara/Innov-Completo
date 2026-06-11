import React from 'react';
import { CheckCircle, Clock } from 'lucide-react';

interface TaskData {
  taskId: number;
  title: string;
  description: string;
  status: string;
}

interface ProjectTaskCardProps {
  task: TaskData;
}

export const ProjectTaskCard: React.FC<ProjectTaskCardProps> = ({ task }) => {
  const isCompleted = task.status === 'COMPLETED' || task.status === 'TERMINADO';

  return (
    <div className="group p-6 bg-white/40 backdrop-blur-md border border-white/70 rounded-3xl hover:bg-white/60 hover:border-white transition-all shadow-sm shadow-neutral-200/50 flex flex-col justify-between">
      <div>
        <div className="flex items-center justify-between mb-4">
          <span className="text-[10px] font-mono text-neutral-400 font-bold bg-neutral-900/5 px-2 py-0.5 rounded">
            TASK-{task.taskId}
          </span>
          
          <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide border ${
            isCompleted
              ? 'bg-green-500/10 text-green-700 border-green-200/30' 
              : 'bg-amber-500/10 text-amber-700 border-amber-200/30'
          }`}>
            {isCompleted ? (
              <>
                <CheckCircle size={10} /> Completada
              </>
            ) : (
              <>
                <Clock size={10} /> En Proceso
              </>
            )}
          </span>
        </div>

        <h3 className="text-base font-bold text-neutral-800 mb-2 group-hover:text-neutral-900 transition-colors">
          {task.title}
        </h3>
        <p className="text-xs text-neutral-600 leading-relaxed mb-4">
          {task.description}
          </p>
      </div>
    </div>
  );
};