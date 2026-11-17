import { useState } from 'react';

type Pantalla =
  | 'inicio'
  | 'crear-reserva'
  | 'aforo-completo'
  | 'reserva-duplicada'
  | 'reserva-confirmada'
  | 'mis-reservas'
  | 'modificar-reserva'
  | 'cancelar-reserva';

type EstadoReserva = 'Confirmada' | 'Cancelada' | 'Finalizada';

interface Reserva {
  id: number;
  codigo: string;
  nombre: string;
  apellidos: string;
  email: string;
  telefono: string;
  fecha: string;
  hora: string;
  comensales: number;
  ninos: number;
  estado: EstadoReserva;
}

interface FormState {
  nombre: string;
  apellidos: string;
  telefono: string;
  email: string;
  personas: number;
  ninos: number;
  fecha: string;
  hora: string;
}

const FORMATO_FECHA = '2026-10-02';
const HORARIOS = ['11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00', '19:30', '20:00', '20:30', '21:00', '21:30', '22:00', '22:30', '23:00'];

const defaultForm: FormState = {
  nombre: 'Lucía',
  apellidos: 'García',
  telefono: '612 345 678',
  email: 'lucia@email.com',
  personas: 4,
  ninos: 0,
  fecha: FORMATO_FECHA,
  hora: '20:30',
};

const reservasIniciales: Reserva[] = [
  {
    id: 1,
    codigo: '4821',
    nombre: 'Lucía',
    apellidos: 'García',
    email: 'lucia@email.com',
    telefono: '612 345 678',
    fecha: '2026-10-02',
    hora: '20:30',
    comensales: 4,
    ninos: 0,
    estado: 'Confirmada',
  },
  {
    id: 2,
    codigo: '1047',
    nombre: 'Daniel',
    apellidos: 'López',
    email: 'daniel@email.com',
    telefono: '623 123 456',
    fecha: '2026-10-05',
    hora: '21:00',
    comensales: 2,
    ninos: 0,
    estado: 'Confirmada',
  },
];

const formatDate = (date: string) => {
  const parsed = new Date(`${date}T12:00:00`);
  return parsed.toLocaleDateString('es-ES', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
};

const createCode = () => Math.floor(1000 + Math.random() * 9000).toString();

function App() {
  const [pantalla, setPantalla] = useState<Pantalla>('inicio');
  const [form, setForm] = useState<FormState>(defaultForm);
  const [reservas, setReservas] = useState<Reserva[]>(reservasIniciales);
  const [reservaSeleccionada, setReservaSeleccionada] = useState<Reserva | null>(null);
  const [modalCancelar, setModalCancelar] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const updateFormField = <K extends keyof FormState>(key: K, value: FormState[K]) => {
    setForm((current) => ({ ...current, [key]: value }));
  };

  const IrInicio = () => {
    setPantalla('inicio');
    setErrorMessage('');
  };

  const CrearReserva = () => {
    const nombre = form.nombre.trim();
    const apellidos = form.apellidos.trim();
    const email = form.email.trim();
    const telefono = form.telefono.trim();

    if (!nombre || !apellidos || !email || !telefono || !form.fecha || !form.hora) {
      setErrorMessage('Rellena todos los campos obligatorios.');
      return;
    }

    const emailOk = /^[\w.-]+@[\w.-]+\.[A-Za-z]{2,}$/.test(email);
    const telefonoOk = /^[0-9\s+()-]{9,}$/.test(telefono);

    if (!emailOk || !telefonoOk) {
      setErrorMessage('Introduce un teléfono y un correo válidos.');
      return;
    }

    if (form.personas < 1 || form.personas > 12) {
      setErrorMessage('Selecciona un número de comensales válido.');
      return;
    }

    const duplicada = reservas.some(
      (reserva) =>
        reserva.email.toLowerCase() === email.toLowerCase() &&
        reserva.fecha === form.fecha &&
        reserva.estado === 'Confirmada',
    );

    if (duplicada) {
      setPantalla('reserva-duplicada');
      setErrorMessage('Ya tienes una reserva para este día.');
      return;
    }

    const reservasMismaHora = reservas.filter(
      (reserva) => reserva.fecha === form.fecha && reserva.hora === form.hora && reserva.estado === 'Confirmada',
    );

    const totalComensales = reservasMismaHora.reduce((total, reserva) => total + reserva.comensales, 0);

    if (totalComensales + form.personas > 60) {
      setPantalla('aforo-completo');
      setErrorMessage('No quedan plazas suficientes para ese horario.');
      return;
    }

    const nuevaReserva: Reserva = {
      id: Date.now(),
      codigo: createCode(),
      nombre,
      apellidos,
      email,
      telefono,
      fecha: form.fecha,
      hora: form.hora,
      comensales: form.personas,
      ninos: form.ninos,
      estado: 'Confirmada',
    };

    setReservas((actual) => [nuevaReserva, ...actual]);
    setReservaSeleccionada(nuevaReserva);
    setErrorMessage('');
    setPantalla('reserva-confirmada');
  };

  const ModificarReserva = () => {
    if (!reservaSeleccionada) return;

    const nombre = form.nombre.trim();
    const apellidos = form.apellidos.trim();
    const email = form.email.trim();
    const telefono = form.telefono.trim();

    if (!nombre || !apellidos || !email || !telefono) {
      setErrorMessage('Debes completar todos los datos antes de guardar.');
      return;
    }

    const reservaActualizada: Reserva = {
      ...reservaSeleccionada,
      nombre,
      apellidos,
      email,
      telefono,
      fecha: form.fecha,
      hora: form.hora,
      comensales: form.personas,
      ninos: form.ninos,
    };

    setReservas((actual) => 
      actual.map((reserva) => (reserva.id === reservaActualizada.id ? reservaActualizada : reserva)),
    );
    setReservaSeleccionada(reservaActualizada);
    setErrorMessage('');
    setPantalla('reserva-confirmada');
  };

  const CancelarReserva = () => {
    if (!reservaSeleccionada) return;

    setReservas((actual) =>
      actual.map((reserva) =>
        reserva.id === reservaSeleccionada.id ? { ...reserva, estado: 'Cancelada' } : reserva,
      ),
    );
    setModalCancelar(false);
    setPantalla('mis-reservas');
  };

  const abrirModificar = (reserva: Reserva) => {
    setReservaSeleccionada(reserva);
    setForm({
      nombre: reserva.nombre,
      apellidos: reserva.apellidos,
      telefono: reserva.telefono,
      email: reserva.email,
      personas: reserva.comensales,
      ninos: reserva.ninos,
      fecha: reserva.fecha,
      hora: reserva.hora,
    });
    setPantalla('modificar-reserva');
  };

  const abrirCancelar = (reserva: Reserva) => {
    setReservaSeleccionada(reserva);
    setModalCancelar(true);
  };

  const renderCabecera = (showBack = true) => (
    <header className="topbar flex items-center justify-between px-6 py-5 sm:px-8 lg:px-12">
      <button
        type="button"
        onClick={IrInicio}
        className="logo text-[2.1rem] font-medium tracking-wider text-[#f2d3a8] transition hover:opacity-80"
      >
        TERRAMÍA
      </button>
      {showBack ? (
        <button
          type="button"
          onClick={() => setPantalla('inicio')}
          className="menu-btn flex h-12 w-12 items-center justify-center border border-[rgba(255,255,255,0.54)] bg-black/10 text-xl text-[#f5efe7]"
          aria-label="Volver al inicio"
        >
          ☰
        </button>
      ) : null}
    </header>
  );

  if (pantalla === 'inicio') {
    return (
      <div className="page-shell page-home">
        {renderCabecera()}
        <main className="mx-auto max-w-[1400px] px-6 pb-10 pt-4 sm:px-8 lg:px-12">
          <div className="flex flex-col gap-8 pb-6 pt-4 lg:pt-10">
            <p className="text-[0.72rem] font-semibold uppercase tracking-[0.35em] text-[#e0d4b0]">
              El arte culinario de la cocina · Valencia
            </p>

            <div className="grid gap-6 lg:grid-cols-[1.3fr_0.7fr] lg:items-end">
              <div className="hero-title-wrap leading-none">
                <h1 className="hero-title">
                  TERRA
                  <span className="hero-title-accent">MIA</span>
                </h1>
                <div className="mt-5 max-w-[520px] border-l border-[#d7bf88] pl-3 text-[1.2rem] italic text-[#efe8dc]">
                  Una mesa, buena compañía<br />
                  y tiempo para disfrutar.
                </div>
              </div>

              <div className="flex flex-col items-end justify-end gap-5 pb-4">
                <div className="h-[1px] w-full max-w-[400px] bg-[rgba(224,212,176,0.5)]"></div>
                <div className="w-full max-w-[420px]">
                  <div className="space-y-4">
                    <button
                      type="button"
                      onClick={() => setPantalla('crear-reserva')}
                      className="gold-button flex w-full items-center justify-between px-6 py-5 text-left text-[0.8rem] font-semibold uppercase tracking-[0.12em]"
                    >
                      <span>Reservar una mesa</span>
                      <span className="text-lg">→</span>
                    </button>
                    <button
                      type="button"
                      onClick={() => setPantalla('mis-reservas')}
                      className="dark-button mis-reservas-button flex w-full items-center justify-between"
                    >
                      <span>Mis reservas</span>
                      <span className="text-lg">→</span>
                    </button>
                    <div className="inline-flex w-full items-center justify-center border border-[rgba(255,255,255,0.45)] px-4 py-3 text-[0.68rem] uppercase tracking-[0.22em] text-[#f6f0e8]">
                      Abierto todos los días · 11:30 – 23:30
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (pantalla === 'crear-reserva' || pantalla === 'modificar-reserva') {
    return (
      <div className="page-shell page-create">
        {renderCabecera()}
        <main className="mx-auto max-w-[1200px] px-4 pb-8 pt-10 sm:px-8 lg:px-10">
          <div className="mx-auto max-w-[1100px]">
            <div className="mb-6 flex items-center gap-3 text-[0.7rem] font-semibold uppercase tracking-[0.25em] text-[#e7d7b0]">
              <button type="button" onClick={IrInicio} className="inline-flex items-center gap-2">
                ← Volver
              </button>
            </div>

            <div className="grid gap-8 lg:grid-cols-[1.1fr_0.9fr] lg:items-start">
              <section className="panel-soft p-5 sm:p-7">
                <p className="mb-6 text-[0.78rem] font-semibold uppercase tracking-[0.35em] text-[#dfc38a]">
                  {pantalla === 'modificar-reserva' ? 'Editar reserva' : 'Reservas'}
                </p>
                <h2 className="mb-3 text-[3.4rem] leading-[0.85] tracking-[-0.05em] text-[#f7f1ea] lg:text-[5rem]">
                  {pantalla === 'modificar-reserva' ? 'Modifica' : 'Reserva'}
                  <span className="block text-[#dfc38a]">tu mesa</span>
                </h2>
                <p className="max-w-md text-base text-[#ece4d7] opacity-80">
                  Completa los datos y elige la fecha y hora que prefieras.
                </p>

                <div className="mt-8 space-y-4">
                  <div className="grid gap-4 sm:grid-cols-2">
                    <label className="form-field">
                      <span>Nombre *</span>
                      <input value={form.nombre} onChange={(event) => updateFormField('nombre', event.target.value)} />
                    </label>
                    <label className="form-field">
                      <span>Apellidos *</span>
                      <input value={form.apellidos} onChange={(event) => updateFormField('apellidos', event.target.value)} />
                    </label>
                  </div>

                  <div className="grid gap-4 sm:grid-cols-2">
                    <label className="form-field">
                      <span>Número de móvil *</span>
                      <input value={form.telefono} onChange={(event) => updateFormField('telefono', event.target.value)} />
                    </label>
                    <label className="form-field">
                      <span>Correo electrónico *</span>
                      <input value={form.email} onChange={(event) => updateFormField('email', event.target.value)} />
                    </label>
                  </div>

                  <div className="mt-5">
                    <p className="mb-3 text-[0.74rem] font-semibold uppercase tracking-[0.3em] text-[#dfc38a]">
                      Número de personas
                    </p>
                    <div className="panel-counter flex items-center justify-between gap-3">
                      <button type="button" onClick={() => updateFormField('personas', Math.max(1, form.personas - 1))}>−</button>
                      <span>{form.personas}</span>
                      <button type="button" onClick={() => updateFormField('personas', form.personas + 1)}>+</button>
                    </div>
                  </div>

                  <div className="mt-4">
                    <p className="mb-3 text-[0.74rem] font-semibold uppercase tracking-[0.3em] text-[#dfc38a]">
                      Niños menores de 3 años
                    </p>
                    <div className="panel-counter small flex items-center justify-between gap-3">
                      <button type="button" onClick={() => updateFormField('ninos', Math.max(0, form.ninos - 1))}>−</button>
                      <span>{form.ninos}</span>
                      <button type="button" onClick={() => updateFormField('ninos', form.ninos + 1)}>+</button>
                    </div>
                  </div>
                </div>
              </section>

              <section className="panel-surface p-5 sm:p-7">
                <div className="mb-5 flex items-center gap-3 text-[0.72rem] font-semibold uppercase tracking-[0.25em] text-[#dfc38a]">
                  <span className="badge">Fecha</span>
                </div>

                <div className="calendar-card">
                  <div className="calendar-header">
                    <button type="button" className="icon-arrow">←</button>
                    <span>Octubre de 2026</span>
                    <button type="button" className="icon-arrow">→</button>
                  </div>
                  <div className="calendar-grid">
                    {['L', 'M', 'X', 'J', 'V', 'S', 'D'].map((day) => (
                      <span key={day} className="calendar-weekday">{day}</span>
                    ))}
                    {[19, 20, 21, 22, 23, 24, 25].map((day) => (
                      <button
                        key={day}
                        type="button"
                        className={day === 2 ? 'calendar-day selected' : 'calendar-day'}
                        onClick={() => updateFormField('fecha', `2026-10-${day.toString().padStart(2, '0')}`)}
                      >
                        {day}
                      </button>
                    ))}
                    {[26, 27, 28, 29, 30, 31].map((day) => (
                      <button
                        key={day}
                        type="button"
                        className="calendar-day"
                        onClick={() => updateFormField('fecha', `2026-10-${day.toString().padStart(2, '0')}`)}
                      >
                        {day}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="mt-7">
                  <p className="mb-3 text-[0.72rem] font-semibold uppercase tracking-[0.25em] text-[#dfc38a]">
                    Hora
                  </p>
                  <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
                    {HORARIOS.map((slot) => (
                      <button
                        key={slot}
                        type="button"
                        className={slot === form.hora ? 'time-slot selected' : 'time-slot'}
                        onClick={() => updateFormField('hora', slot)}
                      >
                        {slot}
                      </button>
                    ))}
                  </div>
                </div>

                {errorMessage ? (
                  <div className="mt-6 rounded-md border border-[#d9c18a] bg-[#f5dec0]/10 px-3 py-2 text-sm text-[#f0d7a3]">
                    {errorMessage}
                  </div>
                ) : null}

                <button
                  type="button"
                  onClick={pantalla === 'modificar-reserva' ? ModificarReserva : CrearReserva}
                  className="mt-8 flex w-full items-center justify-between bg-[#0e0d0b] px-6 py-4 text-[0.74rem] font-semibold uppercase tracking-[0.2em] text-[#f5efe7] transition hover:opacity-90"
                >
                  <span>{pantalla === 'modificar-reserva' ? 'Guardar cambios' : 'Confirmar reserva'}</span>
                  <span className="text-xl">→</span>
                </button>
              </section>
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (pantalla === 'aforo-completo') {
    return (
      <div className="page-shell page-home">
        {renderCabecera()}
        <main className="mx-auto flex min-h-[70vh] max-w-[700px] items-center justify-center px-6 pb-10 pt-10">
          <div className="modal-card w-full p-6 sm:p-8">
            <div className="mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-full border border-[#d6b57f] text-3xl text-[#d6b57f]">
              !
            </div>
            <p className="mb-5 text-center text-[0.74rem] font-semibold uppercase tracking-[0.3em] text-[#d6b57f]">
              Aforo completo
            </p>
            <h3 className="mb-4 text-center text-[2.7rem] leading-none text-[#151515]">
              No quedan plazas<br />
              para esa hora.
            </h3>
            <p className="mb-8 text-center text-base text-[#3b3b3b]">
              Prueba otra franja horaria o revisa tus reservas actuales.
            </p>
            <div className="flex flex-col gap-4 sm:flex-row">
              <button type="button" onClick={() => setPantalla('crear-reserva')} className="dark-button flex-1">
                Continuar
              </button>
              <button type="button" onClick={() => setPantalla('mis-reservas')} className="secondary-button flex-1">
                Mis reservas
              </button>
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (pantalla === 'reserva-duplicada') {
    return (
      <div className="page-shell page-home">
        {renderCabecera()}
        <main className="mx-auto flex min-h-[70vh] max-w-[700px] items-center justify-center px-6 pb-10 pt-10">
          <div className="modal-card w-full p-6 sm:p-8">
            <div className="mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-full border border-[#d6b57f] text-3xl text-[#d6b57f]">
              !
            </div>
            <p className="mb-5 text-center text-[0.74rem] font-semibold uppercase tracking-[0.3em] text-[#d6b57f]">
              Reserva duplicada
            </p>
            <h3 className="mb-4 text-center text-[2.7rem] leading-none text-[#151515]">
              Ya tienes una reserva
              <span className="block">para {formatDate(form.fecha)}</span>
            </h3>
            <p className="mb-8 text-center text-base text-[#3b3b3b]">
              ¿Quieres continuar o modificar la reserva existente?
            </p>
            <div className="flex flex-col gap-4 sm:flex-row">
              <button type="button" onClick={() => setPantalla('mis-reservas')} className="dark-button flex-1">
                Continuar
              </button>
              <button type="button" onClick={() => setPantalla('crear-reserva')} className="secondary-button flex-1">
                Modificar
              </button>
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (pantalla === 'reserva-confirmada') {
    const actual = reservaSeleccionada ?? reservas[0];

    return (
      <div className="page-shell page-confirmation">
        {renderCabecera()}
        <main className="mx-auto flex max-w-[820px] flex-col items-center justify-center px-6 py-20 text-center">
          <div className="mb-8 flex h-20 w-20 items-center justify-center rounded-full border-2 border-[#66b67e] bg-[#66b67e]/10 text-4xl text-[#66b67e] shadow-[0_0_0_8px_rgba(102,182,126,0.12)] transition duration-200 hover:scale-105 hover:shadow-[0_0_0_12px_rgba(102,182,126,0.18)]">
            ✓
          </div>
          <p className="text-[0.72rem] font-semibold uppercase tracking-[0.35em] text-[#66b67e]">
            Reserva confirmada
          </p>
          <h2 className="mt-8 text-[4rem] leading-[0.9] tracking-[-0.06em] text-[#171717] lg:text-[6rem]">
            Tu mesa te espera.
          </h2>
          <p className="mt-6 text-lg text-[#3e3a37]">
            Hemos enviado la confirmación y todos los detalles a{' '}
            <span className="font-medium text-[#171717]">{actual?.email}</span>.
          </p>

          <div className="mt-12 grid w-full max-w-[640px] gap-0 border border-[rgba(19,19,19,0.4)] bg-white/10 md:grid-cols-4">
            <div className="summary-cell">
              <span>Reserva n°</span>
              <strong>{actual?.codigo}</strong>
            </div>
            <div className="summary-cell">
              <span>Fecha</span>
              <strong>{actual ? formatDate(actual.fecha) : '-'}</strong>
            </div>
            <div className="summary-cell">
              <span>Hora</span>
              <strong>{actual?.hora}</strong>
            </div>
            <div className="summary-cell">
              <span>Personas</span>
              <strong>{actual?.comensales}</strong>
            </div>
          </div>

          <button
            type="button"
            onClick={IrInicio}
            className="mt-10 w-full max-w-[420px] bg-[#0d0d0c] px-6 py-4 text-[0.82rem] font-semibold uppercase tracking-[0.22em] text-[#f5efe7] transition hover:opacity-90"
          >
            Volver al inicio
          </button>
        </main>
      </div>
    );
  }

  if (pantalla === 'mis-reservas') {
    return (
      <div className="page-shell page-mis-reservas">
        {renderCabecera()}
        <main className="mx-auto max-w-[1300px] px-4 pb-12 pt-10 sm:px-8 lg:px-10">
          <div className="mb-6 flex items-center gap-3 text-[0.7rem] font-semibold uppercase tracking-[0.25em] text-[#e6d0a3]">
            <button type="button" onClick={IrInicio} className="inline-flex items-center gap-2">
              ← Volver
            </button>
          </div>

          <div className="mb-8">
            <p className="mb-3 text-[0.7rem] font-semibold uppercase tracking-[0.32em] text-[#dfc38a]">
              Tu espacio
            </p>
            <h2 className="text-[3.7rem] leading-[0.9] tracking-[-0.06em] text-[#f7f1ea] lg:text-[6rem]">
              Mis reservas
            </h2>
            <p className="mt-5 text-base text-[#ece4d7] opacity-80">
              Consulta y gestiona tus próximas visitas a Terramía.
            </p>
          </div>

          <div className="mb-8 flex max-w-[420px] items-center gap-3 rounded-lg border border-[#e7d7b0]/40 bg-[#0e0d0b]/40 px-4 py-3 text-[#f7f1ea]">
            <label className="text-[0.7rem] uppercase tracking-[0.24em] text-[#dfc38a]">Número de móvil</label>
            <input className="flex-1 bg-transparent text-base text-[#f4efe9] outline-none placeholder:text-[#d7d0c1]" placeholder="612 345 678" defaultValue="612 345 678" />
            <button type="button" onClick={() => undefined} className="text-[0.72rem] uppercase tracking-[0.18em] text-[#f4efe9]">
              Buscar →
            </button>
          </div>

          <div className="mb-10 flex items-center justify-between">
            <h3 className="text-[2.6rem] leading-none tracking-[-0.06em] text-[#f7f1ea]">Tus reservas</h3>
            <span className="text-[0.67rem] uppercase tracking-[0.28em] text-[#e7d7b0]">
              {reservas.filter((reserv) => reserv.estado === 'Confirmada').length} resultados
            </span>
          </div>

          <div className="space-y-4">
            {reservas.filter((reserv) => reserv.estado !== 'Cancelada').map((reserva) => (
              <div key={reserva.id} className="reservation-card">
                <div className="reservation-meta">
                  <span className="text-[0.64rem] uppercase tracking-[0.3em] text-[#d9c18a]">
                    Reserva n° {reserva.codigo}
                  </span>
                  <div className="mt-3 flex items-baseline gap-4">
                    <span className="reservation-number">{new Date(`${reserva.fecha}T12:00:00`).getDate()}</span>
                    <span className="reservation-date">
                      {new Date(`${reserva.fecha}T12:00:00`).toLocaleDateString('es-ES', {
                        month: 'short',
                        year: 'numeric',
                      })}
                    </span>
                  </div>
                </div>
                <div className="reservation-attribute">
                  <span>Hora</span>
                  <strong>{reserva.hora}</strong>
                </div>
                <div className="reservation-attribute">
                  <span>Personas</span>
                  <strong>{reserva.comensales}</strong>
                </div>
                <div className="reservation-status">
                  <span>{reserva.estado}</span>
                </div>
                <div className="reservation-actions">
                  <button type="button" onClick={() => abrirModificar(reserva)} className="action-button light">
                    Modificar
                  </button>
                  <button type="button" onClick={() => abrirCancelar(reserva)} className="action-button dark">
                    Cancelar
                  </button>
                </div>
              </div>
            ))}
          </div>

          {modalCancelar && reservaSeleccionada ? (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
              <div className="modal-card max-w-[460px] w-full p-6 sm:p-8">
                <div className="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-full border border-[#d6b57f] text-2xl text-[#d6b57f]">
                  !
                </div>
                <p className="mb-4 text-center text-[0.7rem] font-semibold uppercase tracking-[0.28em] text-[#d6b57f]">
                  Cancelar reserva
                </p>
                <h4 className="mb-6 text-center text-[2.2rem] leading-none text-[#171717]">
                  ¿Seguro que deseas cancelar esta reserva?
                </h4>
                <div className="flex gap-3">
                  <button type="button" onClick={() => setModalCancelar(false)} className="secondary-button flex-1">
                    No
                  </button>
                  <button type="button" onClick={CancelarReserva} className="dark-button flex-1">
                    Sí
                  </button>
                </div>
              </div>
            </div>
          ) : null}
        </main>
      </div>
    );
  }

  return null;
}

export default App;
