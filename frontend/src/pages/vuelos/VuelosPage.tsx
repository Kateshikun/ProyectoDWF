import { useEffect, useState, type FormEvent } from 'react';
import { vueloService } from '../../api/services/vuelo.service';
import { useAuth } from '../../context/AuthContext';
import { useApi } from '../../hooks/useApi';
import DataTable, { type Column } from '../../components/shared/DataTable';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Badge from '../../components/ui/Badge';
import ConfirmDialog from '../../components/shared/ConfirmDialog';
import EmptyState from '../../components/ui/EmptyState';
import { Plane, Plus, Search, Trash2 } from 'lucide-react';
import type { VueloResponse, VueloDto } from '../../api/types';
import { formatDateTime } from '../../utils/format';
import toast from 'react-hot-toast';

const ESTADOS_VUELO = [
  { value: 'Programado', label: 'Programado' },
  { value: 'Retrasado', label: 'Retrasado' },
  { value: 'Cancelado', label: 'Cancelado' },
];

export default function VuelosPage() {
  const { isAdmin } = useAuth();
  const { loading, execute } = useApi();
  const [vuelos, setVuelos] = useState<VueloResponse[]>([]);
  const [showCreate, setShowCreate] = useState(false);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [searchParams, setSearchParams] = useState({ origen: '', destino: '', fecha: '' });
  const [form, setForm] = useState<VueloDto>({
    id_ruta: 0, id_avion: 0, fecha_salida: '', fecha_llegada: '',
    estado: 'Programado', nombre_piloto: '', nombre_copiloto: '',
  });

  const loadVuelos = async () => {
    const data = await execute(() => vueloService.listar());
    if (data) setVuelos(data);
  };

  useEffect(() => { loadVuelos(); }, []);

  const handleSearch = async (e: FormEvent) => {
    e.preventDefault();
    if (!searchParams.origen || !searchParams.destino || !searchParams.fecha) {
      loadVuelos();
      return;
    }
    const data = await execute(() =>
      vueloService.buscar(searchParams.origen, searchParams.destino, searchParams.fecha)
    );
    if (data) setVuelos(data);
  };

  const handleCreate = async (e: FormEvent) => {
    e.preventDefault();
    const data = await execute(() => vueloService.crear(form));
    if (data) {
      toast.success('Vuelo creado exitosamente');
      setShowCreate(false);
      setForm({ id_ruta: 0, id_avion: 0, fecha_salida: '', fecha_llegada: '', estado: 'Programado', nombre_piloto: '', nombre_copiloto: '' });
      loadVuelos();
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    const ok = await execute(() => vueloService.eliminar(deleteId));
    if (ok !== null) {
      toast.success('Vuelo eliminado');
      setDeleteId(null);
      loadVuelos();
    }
  };

  const handleStatusChange = async (id: number, estado: string) => {
    const ok = await execute(() => vueloService.actualizarEstado(id, estado));
    if (ok !== null) {
      toast.success('Estado actualizado');
      loadVuelos();
    }
  };

  const statusBadge = (estado: string | null) => {
    if (!estado) return <Badge variant="neutral">-</Badge>;
    const map: Record<string, 'success' | 'warning' | 'error'> = {
      Programado: 'success', Retrasado: 'warning', Cancelado: 'error',
    };
    return <Badge variant={map[estado] || 'neutral'}>{estado}</Badge>;
  };

  const columns: Column<VueloResponse>[] = [
    { key: 'origen', header: 'Origen' },
    { key: 'destino', header: 'Destino' },
    { key: 'fecha_salida', header: 'Salida', render: (v) => formatDateTime(v.fecha_salida) },
    { key: 'fecha_llegada', header: 'Llegada', render: (v) => formatDateTime(v.fecha_llegada) },
    { key: 'estado', header: 'Estado', render: (v) => statusBadge(v.estado) },
    { key: 'nombre_piloto', header: 'Piloto' },
    { key: 'capacidad_pasajeros', header: 'Capacidad' },
  ];

  if (isAdmin) {
    columns.push({
      key: 'actions',
      header: 'Acciones',
      render: (v) => (
        <div className="flex items-center gap-2">
          <Select
            value={v.estado || ''}
            onChange={(e) => handleStatusChange(v.id_vuelo, e.target.value)}
            options={ESTADOS_VUELO}
            className="w-32"
          />
          <button onClick={() => setDeleteId(v.id_vuelo)} className="p-1.5 rounded-lg hover:bg-error-50 text-error-600 transition-colors">
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      ),
    });
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-900">Vuelos</h2>
        {isAdmin && (
          <Button onClick={() => setShowCreate(true)}><Plus className="h-4 w-4" /> Nuevo vuelo</Button>
        )}
      </div>

      <Card>
        <form onSubmit={handleSearch} className="flex flex-wrap gap-3 items-end mb-6">
          <Input label="Ciudad origen" value={searchParams.origen} onChange={(e) => setSearchParams((p) => ({ ...p, origen: e.target.value }))} placeholder="Ej: San Salvador" className="w-44" />
          <Input label="Ciudad destino" value={searchParams.destino} onChange={(e) => setSearchParams((p) => ({ ...p, destino: e.target.value }))} placeholder="Ej: Miami" className="w-44" />
          <Input label="Fecha" type="datetime-local" value={searchParams.fecha} onChange={(e) => setSearchParams((p) => ({ ...p, fecha: e.target.value }))} className="w-52" />
          <Button type="submit" variant="secondary" size="sm"><Search className="h-4 w-4" /> Buscar</Button>
        </form>

        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full" /></div>
        ) : vuelos.length === 0 ? (
          <EmptyState icon={<Plane className="h-12 w-12" />} title="Sin vuelos" description="No se encontraron vuelos" />
        ) : (
          <DataTable columns={columns} data={vuelos} keyExtractor={(v) => v.id_vuelo} />
        )}
      </Card>

      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="Crear vuelo" maxWidth="max-w-xl">
        <form onSubmit={handleCreate} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input label="ID Ruta" type="number" value={form.id_ruta || ''} onChange={(e) => setForm((p) => ({ ...p, id_ruta: Number(e.target.value) }))} />
            <Input label="ID Avion" type="number" value={form.id_avion || ''} onChange={(e) => setForm((p) => ({ ...p, id_avion: Number(e.target.value) }))} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Fecha salida" type="datetime-local" value={form.fecha_salida} onChange={(e) => setForm((p) => ({ ...p, fecha_salida: e.target.value }))} />
            <Input label="Fecha llegada" type="datetime-local" value={form.fecha_llegada} onChange={(e) => setForm((p) => ({ ...p, fecha_llegada: e.target.value }))} />
          </div>
          <Select label="Estado" value={form.estado} onChange={(e) => setForm((p) => ({ ...p, estado: e.target.value }))} options={ESTADOS_VUELO} />
          <Input label="Piloto" value={form.nombre_piloto} onChange={(e) => setForm((p) => ({ ...p, nombre_piloto: e.target.value }))} placeholder="Nombre del piloto" />
          <Input label="Copiloto" value={form.nombre_copiloto} onChange={(e) => setForm((p) => ({ ...p, nombre_copiloto: e.target.value }))} placeholder="Nombre del copiloto" />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowCreate(false)}>Cancelar</Button>
            <Button type="submit" loading={loading}>Crear vuelo</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        title="Eliminar vuelo"
        message="Esta seguro de eliminar este vuelo? Esta accion no se puede deshacer."
        confirmLabel="Eliminar"
        loading={loading}
      />
    </div>
  );
}
