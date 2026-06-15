import api from '../api/axios';

export interface TaskStatus {
  idTaskStatus: number;
  status: string;
}

export interface Task {
  idTask?: number;
  name: string;
  description: string;
  dateCreated: string; // Formato dd-MM-yyyy
  dateFinished?: string;
  projectId: number;
  taskStatus: TaskStatus;
}

export const getTasks = async (): Promise<Task[]> => {
  const response = await api.get('/tasks/all');
  if (response.status === 200) return response.data;
  return [];
};

export const createTask = async (payload: Task): Promise<Task | null> => {
  const response = await api.post('/tasks/save', payload);
  if (response.status === 200 || response.status === 201) return response.data;
  return null;
};

export const deleteTask = async (id: number): Promise<void> => {
  await api.delete(`/tasks/${id}`);
};