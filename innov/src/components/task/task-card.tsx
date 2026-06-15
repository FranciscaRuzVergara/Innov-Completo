import React from 'react';
import { Trash2, Layers, Calendar, Folder } from 'lucide-react';
import { type Task } from '@/services/task-service';

interface TaskCardProps {
  item: Task;
  onDelete: (id: number) => void;
}

export const TaskCard: React.FC<TaskCardProps> = ({ item, onDelete }) => {
  return (
    <div className="group relative p-8 bg-white/40 backdrop-blur-md border border-white/70 rounded-3xl hover:bg-white/60 hover:border-white transition-all shadow-sm shadow-neutral-200/50">
      <button 
        onClick={() => item.idTask && onDelete(item.idTask)}
        className="absolute top-6 right-6 p-2 bg-red-500/10 text-red-600 opacity-0 group-hover:opacity-100 rounded-xl hover:bg-red-600 hover:text-white transition-all border border-red-200/20"
      >
        <Trash2 size={16} />
      </button>

      <div className="space-y-4">
        <div className="flex items-start gap-3">
          <div className="p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <Layers size={18} className="text-neutral-500" />
          </div>
          <div>
            <span className="text-[9px] font-mono text-neutral-400 font-bold bg-neutral-900/5 px-2 py-0.5 rounded">
              ID-{item.idTask}
            </span>
            <h4 className="text-base font-bold text-neutral-800 mt-1">{item.name}</h4>
            <p className="text-xs text-neutral-500 leading-relaxed mt-1">{item.description}</p>
          </div>
        </div>

        <div className="flex flex-wrap gap-4 border-t border-neutral-200/30 pt-4 text-[11px] font-medium text-neutral-500">
          <div className="flex items-center gap-1">
            <Calendar size={12} />
            <span>Creada: {item.dateCreated}</span>
          </div>
          <div className="flex items-center gap-1 ml-auto">
            <Folder size={12} />
            <span className="font-bold">PRJ: {item.projectId}</span>
          </div>
        </div>
      </div>
    </div>
  );
};