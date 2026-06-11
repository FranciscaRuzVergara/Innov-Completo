import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Users, ClipboardList, FolderKanban, LogOut, ShieldCheck } from 'lucide-react';

export const Navbar: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const currentPath = location.pathname;

  const isActive = (path: string) => {
    if (path === '/projects') {
      return currentPath.startsWith('/projects'); 
    }
    return currentPath === path;
  };

  const handleLogout = () => {
    localStorage.removeItem('token'); 
    navigate('/login');
  };

  if (currentPath === '/login' || currentPath === '/register') return null;
  return (
    <nav className="sticky top-0 z-50 w-full px-6 py-4 bg-white/20 backdrop-blur-md border-b border-white/60 shadow-sm shadow-neutral-200/20 font-sans">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        
        <div className="flex items-center gap-2">
          <span className="p-2 bg-neutral-900 text-white rounded-xl shadow-sm">
            <ShieldCheck size={16} />
          </span>
          <span className="text-xs font-black tracking-widest uppercase text-neutral-800">
            Innovatech
          </span>
        </div>

        <div className="flex items-center gap-2 bg-white/40 border border-white/80 p-1.5 rounded-2xl shadow-sm">
          <Link
            to="/employees"
            className={`flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold uppercase tracking-wider transition-all ${
              isActive('/employees')
                ? 'bg-neutral-900 text-white shadow-sm'
                : 'text-neutral-600 hover:text-neutral-900 hover:bg-white/50'
            }`}
          >
            <Users size={14} /> Empleados
          </Link>

          <Link
            to="/assignments"
            className={`flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold uppercase tracking-wider transition-all ${
              isActive('/assignments')
                ? 'bg-neutral-900 text-white shadow-sm'
                : 'text-neutral-600 hover:text-neutral-900 hover:bg-white/50'
            }`}
          >
            <ClipboardList size={14} /> Asignaciones
          </Link>

          <Link
            to="/projects"
            className={`flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold uppercase tracking-wider transition-all ${
              isActive('/projects')
                ? 'bg-neutral-900 text-white shadow-sm'
                : 'text-neutral-600 hover:text-neutral-900 hover:bg-white/50'
            }`}
          >
            <FolderKanban size={14} /> Proyectos
          </Link>

          <Link
            to="/tasks"
            className={`flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold uppercase tracking-wider transition-all ${
              isActive('/tasks')
                ? 'bg-neutral-900 text-white shadow-sm'
                : 'text-neutral-600 hover:text-neutral-900 hover:bg-white/50'
            }`}
          >
            <ClipboardList size={14} /> Tareas
          </Link>
        </div>


        <button
          onClick={handleLogout}
          className="flex items-center gap-2 px-3 py-2 bg-red-500/10 text-red-600 border border-red-200/20 rounded-xl text-xs font-bold uppercase tracking-wider hover:bg-red-600 hover:text-white transition-all active:scale-[0.98]"
        >
          <LogOut size={14} /> Salir
        </button>

      </div>
    </nav>
  );
};