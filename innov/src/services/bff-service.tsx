import api from '@/api/axios';

export interface ProjectWithTasks {
  projectId: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  tasks: Array<{
    taskId: number;
    title: string;
    description: string;
    status: string;
  }>;
}

export const getDashboardProject = async (projectId: number): Promise<ProjectWithTasks | null> => {
  try {
    const response = await api.get<ProjectWithTasks>(`/bff/${projectId}/tasks`);
    if (response.status === 200) {
      return response.data;
    }
    return null;
  } catch (error) {
    console.error("Error al conectar con el BFF:", error);
    return null;
  }
};