import React, { useState, useEffect } from 'react'
import { Search, Calendar, User, LayoutDashboard, CreditCard, Plus, ChevronRight, CheckCircle2 } from 'lucide-react'
import { hotelService } from './api/api'
import { format } from 'date-fns'

function App() {
  const [view, setView] = useState('search') // 'search', 'booking', 'management'
  const [rooms, setRooms] = useState([])
  const [selectedRoom, setSelectedRoom] = useState(null)
  const [searchParams, setSearchParams] = useState({
    startDate: format(new Date(), 'yyyy-MM-dd'),
    endDate: format(new Date().setDate(new Date().getDate() + 2), 'yyyy-MM-dd'),
    type: ''
  })
  const [loading, setLoading] = useState(false)
  const [reservation, setReservation] = useState(null)

  useEffect(() => {
    if (view === 'search') {
      fetchRooms()
    }
  }, [view])

  const fetchRooms = async () => {
    setLoading(true)
    try {
      const res = await hotelService.getDisponibilidad(searchParams)
      setRooms(res.data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleBook = (room) => {
    setSelectedRoom(room)
    setView('booking')
  }

  return (
    <div className="app-container">
      <header>
        <div className="logo">XLR8 HOTEL</div>
        <nav style={{ display: 'flex', gap: '1.5rem' }}>
          <button className={`btn ${view === 'search' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setView('search')}>
            <Search size={18} /> Explorar
          </button>
          <button className={`btn ${view === 'management' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setView('management')}>
            <LayoutDashboard size={18} /> Mi Reserva
          </button>
        </nav>
      </header>

      <main style={{ padding: '3rem', maxWidth: '1200px', margin: '0 auto' }}>
        {view === 'search' && (
          <div className="animate-fade">
            <h1 style={{ fontSize: '3rem', fontWeight: 800, marginBottom: '2rem' }}>Reserva tu experiencia <span style={{ color: 'var(--primary)' }}>Premium</span></h1>
            
            <section className="glass-card" style={{ marginBottom: '3rem', display: 'flex', gap: '1.5rem', alignItems: 'flex-end' }}>
              <div className="input-group" style={{ flex: 1, margin: 0 }}>
                <label>Fecha de Entrada</label>
                <input type="date" value={searchParams.startDate} onChange={(e) => setSearchParams({...searchParams, startDate: e.target.value})} />
              </div>
              <div className="input-group" style={{ flex: 1, margin: 0 }}>
                <label>Fecha de Salida</label>
                <input type="date" value={searchParams.endDate} onChange={(e) => setSearchParams({...searchParams, endDate: e.target.value})} />
              </div>
              <div className="input-group" style={{ flex: 1, margin: 0 }}>
                <label>Tipo de Habitación</label>
                <select value={searchParams.type} onChange={(e) => setSearchParams({...searchParams, type: e.target.value})}>
                  <option value="">Todas</option>
                  <option value="SINGLE">Sencilla</option>
                  <option value="DOUBLE">Doble</option>
                  <option value="SUITE">Suite</option>
                </select>
              </div>
              <button className="btn btn-primary" style={{ height: '3.1rem' }} onClick={fetchRooms}>
                Buscar Ahora
              </button>
            </section>

            <div className="grid grid-cols-3">
              {rooms.map(room => (
                <div key={room.id} className="glass-card" style={{ padding: 0, overflow: 'hidden' }}>
                  <img src={room.imageUrl} alt={room.type} style={{ width: '100%', height: '200px', objectFit: 'cover' }} />
                  <div style={{ padding: '1.5rem' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1rem' }}>
                      <span style={{ color: 'var(--primary)', fontWeight: 600 }}>{room.type}</span>
                      <span style={{ fontWeight: 800 }}>${room.type === 'SINGLE' ? '100' : room.type === 'DOUBLE' ? '150' : '250'}/noche</span>
                    </div>
                    <h3 style={{ marginBottom: '1rem' }}>Habitación {room.number}</h3>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.875rem', marginBottom: '1.5rem' }}>
                      Disfruta de una estancia inolvidable con todas las comodidades premium de nuestra cadena.
                    </p>
                    <button className="btn btn-primary" style={{ width: '100%' }} onClick={() => handleBook(room)}>
                      Reservar <ChevronRight size={18} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {view === 'booking' && (
          <BookingView 
            room={selectedRoom} 
            searchParams={searchParams} 
            onSuccess={(res) => { setReservation(res); setView('management'); }}
            onCancel={() => setView('search')}
          />
        )}

        {view === 'management' && (
          <ManagementView initialReservation={reservation} />
        )}
      </main>
    </div>
  )
}

// Sub-components as separate components in App.jsx for simplicity or move to files later
function BookingView({ room, searchParams, onSuccess, onCancel }) {
  const [guest, setGuest] = useState({ firstName: '', lastName: '', email: '', idNumber: '' })
  const [saving, setSaving] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      const res = await hotelService.crearReserva({
        roomId: room.id,
        guest: guest,
        startDate: searchParams.startDate,
        endDate: searchParams.endDate
      })
      onSuccess(res.data)
    } catch (err) {
      alert("Error al crear reserva: " + err.message)
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="animate-fade" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h2 style={{ fontSize: '2rem', marginBottom: '2rem' }}>Finaliza tu Reserva</h2>
      <form className="glass-card" onSubmit={handleSubmit}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
          <div className="input-group">
            <label>Nombre</label>
            <input required value={guest.firstName} onChange={e => setGuest({...guest, firstName: e.target.value})} />
          </div>
          <div className="input-group">
            <label>Apellido</label>
            <input required value={guest.lastName} onChange={e => setGuest({...guest, lastName: e.target.value})} />
          </div>
        </div>
        <div className="input-group">
          <label>Email</label>
          <input required type="email" value={guest.email} onChange={e => setGuest({...guest, email: e.target.value})} />
        </div>
        <div className="input-group">
          <label>Documento de Identidad</label>
          <input required value={guest.idNumber} onChange={e => setGuest({...guest, idNumber: e.target.value})} />
        </div>
        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
          <button type="button" className="btn btn-outline" onClick={onCancel} style={{ flex: 1 }}>Cancelar</button>
          <button type="submit" className="btn btn-primary" style={{ flex: 1 }} disabled={saving}>
            {saving ? 'Procesando...' : 'Confirmar Reserva'}
          </button>
        </div>
      </form>
    </div>
  )
}

function ManagementView({ initialReservation }) {
  const [resId, setResId] = useState(initialReservation?.id || '')
  const [reservation, setReservation] = useState(initialReservation)
  const [tab, setTab] = useState('summary') // 'summary', 'invoice'
  const [invoice, setInvoice] = useState(null)

  const fetchReserva = async () => {
    if (!resId) return
    try {
      const res = await hotelService.getReserva(resId)
      setReservation(res.data)
      setInvoice(null)
    } catch (err) {
      alert("Reserva no encontrada")
    }
  }

  const handleAction = async (action) => {
    try {
      if (action === 'checkin') {
        const res = await hotelService.realizarCheckIn(reservation.id)
        setReservation(res.data)
      } else if (action === 'checkout') {
        const res = await hotelService.realizarCheckOut(reservation.id)
        setInvoice(res.data)
        setTab('invoice')
      }
    } catch (err) {
      alert("Error: " + err.message)
    }
  }

  const addService = async (type) => {
    try {
      await hotelService.agregarServicio(reservation.id, type)
      fetchReserva()
    } catch (err) {
      alert("Error al añadir servicio")
    }
  }

  if (!reservation && !invoice) {
    return (
      <div className="animate-fade glass-card" style={{ maxWidth: '500px', margin: '0 auto', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '1.5rem' }}>Buscar mi Reserva</h2>
        <div className="input-group">
          <input placeholder="Introduce tu ID de Reserva (ej: A1B2C3D4)" value={resId} onChange={e => setResId(e.target.value)} />
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} onClick={fetchReserva}>Buscar</button>
      </div>
    )
  }

  return (
    <div className="animate-fade">
      {tab === 'summary' ? (
        <div className="grid" style={{ gridTemplateColumns: '1fr 350px' }}>
          <div className="glass-card">
            <h2 style={{ marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between' }}>
              Gestión de Reserva #{reservation.id}
              <span style={{ fontSize: '0.875rem', padding: '0.25rem 0.75rem', borderRadius: '1rem', background: 'var(--primary)', color: 'white' }}>
                {reservation.status}
              </span>
            </h2>
            
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem', marginBottom: '2rem' }}>
              <div>
                <h4 style={{ color: 'var(--text-muted)', marginBottom: '0.5rem' }}>Huésped</h4>
                <p style={{ fontSize: '1.1rem', fontWeight: 600 }}>{reservation.guest.firstName} {reservation.guest.lastName}</p>
                <p style={{ color: 'var(--text-muted)' }}>{reservation.guest.email}</p>
              </div>
              <div>
                <h4 style={{ color: 'var(--text-muted)', marginBottom: '0.5rem' }}>Fechas</h4>
                <p>{reservation.startDate} hasta {reservation.endDate}</p>
              </div>
            </div>

            {reservation.status === 'PENDING' && (
              <button className="btn btn-primary" onClick={() => handleAction('checkin')} style={{ width: '100%', padding: '1.5rem' }}>
                Realizar <span style={{ fontWeight: 800 }}>Check-In</span>
              </button>
            )}

            {reservation.status === 'CHECKED_IN' && (
              <div>
                <div style={{ background: 'var(--glass)', padding: '1.5rem', borderRadius: '1rem', textAlign: 'center', marginBottom: '2rem', border: '1px dashed var(--primary)' }}>
                  <p style={{ color: 'var(--text-muted)', marginBottom: '0.5rem' }}>Tu LLAVE DIGITAL (UUID)</p>
                  <code style={{ fontSize: '1.2rem', color: 'var(--primary)', fontWeight: 800 }}>{reservation.digitalKey}</code>
                </div>
                
                <h3 style={{ marginBottom: '1rem' }}>Añadir Servicios Extras</h3>
                <div style={{ display: 'flex', gap: '1rem' }}>
                  <button className="btn btn-outline" style={{ flex: 1 }} onClick={() => addService('SPA')}>Spa ($50)</button>
                  <button className="btn btn-outline" style={{ flex: 1 }} onClick={() => addService('BREAKFAST')}>Desayuno ($15)</button>
                  <button className="btn btn-outline" style={{ flex: 1 }} onClick={() => addService('TRANSPORT')}>Traslado ($30)</button>
                </div>

                <button className="btn btn-primary" onClick={() => handleAction('checkout')} style={{ width: '100%', marginTop: '2rem', padding: '1.5rem', background: 'var(--accent)' }}>
                  Realizar <span style={{ fontWeight: 800 }}>Check-Out</span> & Ver Factura
                </button>
              </div>
            )}

            {reservation.status === 'CHECKED_OUT' && (
              <p style={{ textAlign: 'center', color: 'var(--text-muted)' }}>Esta estancia ha finalizado.</p>
            )}
          </div>

          <div className="glass-card">
            <h3 style={{ marginBottom: '1.5rem' }}>Resumen de Servicios</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {reservation.additionalServices.length === 0 ? (
                <p style={{ color: 'var(--text-muted)', fontSize: '0.875rem' }}>No hay servicios adicionales.</p>
              ) : (
                reservation.additionalServices.map((s, i) => (
                  <div key={i} style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', background: 'var(--glass)', borderRadius: '0.5rem' }}>
                    <span>{s}</span>
                    <span style={{ fontWeight: 600 }}>+${s === 'SPA' ? '50' : s === 'BREAKFAST' ? '15' : '30'}</span>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      ) : (
        <div className="glass-card animate-fade" style={{ maxWidth: '700px', margin: '0 auto' }}>
           <h2 style={{ textAlign: 'center', marginBottom: '2rem' }}>Factura Detallada</h2>
           <div style={{ borderBottom: '1px solid var(--border)', paddingBottom: '1.5rem', marginBottom: '1.5rem' }}>
              <p><strong>Nº Factura:</strong> {invoice.id}</p>
              <p><strong>Cliente:</strong> {invoice.guestName}</p>
           </div>
           
           <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ textAlign: 'left', color: 'var(--text-muted)' }}>
                  <th style={{ padding: '0.5rem' }}>Concepto</th>
                  <th style={{ padding: '0.5rem', textAlign: 'right' }}>Total</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td style={{ padding: '0.5rem' }}>Estadía ({invoice.numberOfNights} noches x Mult. {invoice.seasonMultiplier})</td>
                  <td style={{ padding: '0.5rem', textAlign: 'right' }}>${invoice.basePrice.toFixed(2)}</td>
                </tr>
                {invoice.services.map((s, i) => (
                  <tr key={i}>
                    <td style={{ padding: '0.5rem' }}>{s.name}</td>
                    <td style={{ padding: '0.5rem', textAlign: 'right' }}>${s.cost.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
           </table>

           <div style={{ marginTop: '2rem', paddingTop: '1.5rem', borderTop: '2px solid var(--primary)', display: 'flex', justifyContent: 'space-between', fontSize: '1.5rem', fontWeight: 800 }}>
              <span>TOTAL</span>
              <span>${invoice.total.toFixed(2)}</span>
           </div>

           <button className="btn btn-primary" style={{ width: '100%', marginTop: '2rem' }} onClick={() => window.print()}>
            Descargar PDF
           </button>
        </div>
      )}
    </div>
  )
}

export default App
