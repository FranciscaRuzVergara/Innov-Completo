import { http, HttpResponse } from 'msw'

export const handlers = [
  // Intercepta peticiones GET a tu endpoint del BFF
  http.get('*/bff/:id/tasks', ({ params }) => {
    // Devolvemos un JSON idéntico al DTO que espera tu Front
    return HttpResponse.json({
      projectId: params.id,
      name: "Proyecto Simulador en React",
      description: "Probando el Front con MSW",
      startDate: "05-07-2026",
      endDate: "10-07-2026",
      tasks: [
        { idTask: 1, name: "Tarea de prueba Front", description: "Funciona perfectamente", projectId: params.id }
      ]
    })
  }),
]