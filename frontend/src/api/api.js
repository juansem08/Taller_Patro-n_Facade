import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api/hotel',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const hotelService = {
  getDisponibilidad: (params) => api.get('/disponibilidad', { params }),
  crearReserva: (data) => api.post('/reservar', data),
  agregarServicio: (reservaId, tipoServicio) => 
    api.post(`/servicios/${reservaId}?tipoServicio=${tipoServicio}`),
  realizarCheckIn: (reservaId) => api.put(`/checkin/${reservaId}`),
  realizarCheckOut: (reservaId) => api.put(`/checkout/${reservaId}`),
  getReserva: (reservaId) => api.get(`/reserva/${reservaId}`),
};

export default api;
