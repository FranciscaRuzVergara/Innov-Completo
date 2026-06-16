import React from 'react';

interface GlobalLoadingProps {
  message?: string;
}

export const GlobalLoading: React.FC<GlobalLoadingProps> = ({ 
  message = "Cargando información consolidada..." 
}) => {
  return (
    <div className="min-h-screen w-full flex flex-col items-center justify-center bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 font-sans p-6 text-center">
      <div className="flex flex-col items-center gap-4 max-w-xs p-6 bg-white/20 backdrop-blur-md border border-white/40 rounded-3xl shadow-sm">
        <div className="w-10 h-10 border-4 border-neutral-200 border-t-neutral-900 rounded-full animate-spin" />
        
        <p className="text-xs font-semibold tracking-widest uppercase text-neutral-500 animate-pulse mt-2">
          {message}
        </p>
      </div>
    </div>
  );
};