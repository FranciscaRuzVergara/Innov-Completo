import React from 'react';
import { Trash2, Clock, User, Briefcase } from 'lucide-react';

interface Assignment {
  id?: number;
  employeeRut: string;
  taskRoleId: number;
  assignedHours: number;
}

interface AssignmentCardProps {
  item: Assignment;
  onDelete: (id: number) => void;
}

export const AssignmentCard: React.FC<AssignmentCardProps> = ({ item, onDelete }) => {
  return (
    <div className="group relative p-8 bg-white/40 backdrop-blur-md border border-white/70 rounded-3xl hover:bg-white/60 hover:border-white transition-all shadow-sm shadow-neutral-200/50">
      <button 
        onClick={() => item.id && onDelete(item.id)}
        className="absolute top-6 right-6 p-2 bg-red-500/10 text-red-600 opacity-0 group-hover:opacity-100 rounded-xl hover:bg-red-600 hover:text-white transition-all border border-red-200/20"
      >
        <Trash2 size={16} />
      </button>

      <div className="absolute top-6 right-16 flex items-center gap-1.5 px-3 py-1 bg-neutral-900/5 border border-neutral-900/10 rounded-full backdrop-blur-sm">
        <Clock size={12} className="text-neutral-600" />
        <span className="text-[11px] font-bold text-neutral-600">{item.assignedHours} hrs</span>
      </div>

      <div className="space-y-6">
        <div className="flex items-start gap-4">
          <div className="mt-1 p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <User size={20} className="text-neutral-500" />
          </div>
          <div>
            <p className="text-[10px] font-bold text-neutral-400 uppercase tracking-wider">Empleado (RUT)</p>
            <p className="text-base font-semibold text-neutral-800">{item.employeeRut}</p>
          </div>
        </div>
        <div className="flex items-start gap-4">
          <div className="mt-1 p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <Briefcase size={20} className="text-neutral-500" />
          </div>
          <div>
            <p className="text-[10px] font-bold text-neutral-400 uppercase tracking-wider">ID Rol de Tarea</p>
            <p className="text-base font-mono font-medium text-neutral-700">#{item.taskRoleId}</p>
          </div>
        </div>
      </div>
    </div>
  );
};