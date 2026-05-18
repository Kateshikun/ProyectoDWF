import { useEffect, useState, type FormEvent } from 'react';
import { pagoService } from '../../api/services/pago.service';
import { useApi } from '../../hooks/useApi';
import DataTable, { type Column } from '../../components/shared/DataTable';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Modal from '../../components/ui/Modal';
import EmptyState from '../../components/ui/EmptyState';
import { CreditCard, Plus, Search } from 'lucide-react';
import type { PagoResponse } from '../../api/types';
import { formatDateTime, formatCurrency } from '../../utils/format';
import toast from 'react-hot-toast';

export default function PagosPage() {
  const { loading, execute } = useApi();
  const [pagos, setPagos] = useState<PagoResponse[]>([]);
  const [pagoDetalle, setPagoDetalle] = useState<PagoResponse | null>(null);
  const [reservaId, setReservaId] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ idReservacion: 0, monto: 0, numeroTarjeta: '', cvv: '' });

  const loadPagos = async () => {
    const data = await execute(() => pagoService.listar());
    if (data) setPagos(data);
  };

  useEffect(() => { loadPagos(); }, []);

  const handleSearch = async (e: FormEvent) => {
    e.preventDefault();
    if (!reservaId) { setPagoDetalle(null); return; }
    const data = await execute(() => pagoService.consultarPorReserva(Number(reservaId)));
    if (data) setPagoDetalle(data);
    else setPagoDetalle(null);
  };

  const handleProcess = async (e: FormEvent) => {
    e.preventDefault();
    const result = await execute(() => pagoService.procesar(form));
    if (result !== null) {
      toast.success('Pago procesado exitosamente');
      setShowForm(false);
      setForm({ idReservacion: 0, monto: 0, numeroTarjeta: '', cvv: '' });
      loadPagos();
    }
  };

  const columns: Column<PagoResponse>[] = [
    { key: 'idPago', header: 'ID Pago' },
    { key: 'idReservacion', header: 'ID Reservacion' },
    { key: 'monto', header: 'Monto', render: (p) => formatCurrency(p.monto) },
    { key: 'fecha_pago', header: 'Fecha', render: (p) => formatDateTime(p.fecha_pago) },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-900">Pagos</h2>
        <Button onClick={() => setShowForm(true)}><Plus className="h-4 w-4" /> Procesar pago</Button>
      </div>

      <Card>
        <form onSubmit={handleSearch} className="flex gap-3 items-end mb-6">
          <Input label="ID Reservacion" type="number" value={reservaId} onChange={(e) => setReservaId(e.target.value)} placeholder="Buscar pago por reservacion" className="w-56" />
          <Button type="submit" variant="secondary" size="sm"><Search className="h-4 w-4" /> Consultar</Button>
        </form>

        {pagoDetalle && (
          <div className="bg-primary-50 rounded-lg p-6 mb-6">
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
              <div>
                <p className="text-sm text-slate-500">ID Pago</p>
                <p className="text-lg font-semibold text-slate-900">{pagoDetalle.idPago}</p>
              </div>
              <div>
                <p className="text-sm text-slate-500">Monto</p>
                <p className="text-lg font-semibold text-slate-900">{formatCurrency(pagoDetalle.monto)}</p>
              </div>
              <div>
                <p className="text-sm text-slate-500">Fecha</p>
                <p className="text-lg font-semibold text-slate-900">{formatDateTime(pagoDetalle.fecha_pago)}</p>
              </div>
            </div>
          </div>
        )}

        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full" /></div>
        ) : pagos.length === 0 ? (
          <EmptyState icon={<CreditCard className="h-12 w-12" />} title="Sin pagos" description="No se encontraron pagos registrados" />
        ) : (
          <DataTable columns={columns} data={pagos} keyExtractor={(p) => p.idPago} />
        )}
      </Card>

      <Modal open={showForm} onClose={() => setShowForm(false)} title="Procesar pago">
        <form onSubmit={handleProcess} className="space-y-4">
          <Input label="ID Reservacion" type="number" value={form.idReservacion || ''} onChange={(e) => setForm((p) => ({ ...p, idReservacion: Number(e.target.value) }))} />
          <Input label="Monto" type="number" step="0.01" min="0.01" value={form.monto || ''} onChange={(e) => setForm((p) => ({ ...p, monto: Number(e.target.value) }))} />
          <Input label="Numero de tarjeta" value={form.numeroTarjeta} onChange={(e) => setForm((p) => ({ ...p, numeroTarjeta: e.target.value }))} placeholder="16 digitos" maxLength={16} />
          <Input label="CVV" value={form.cvv} onChange={(e) => setForm((p) => ({ ...p, cvv: e.target.value }))} placeholder="3 digitos" maxLength={3} />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowForm(false)}>Cancelar</Button>
            <Button type="submit" loading={loading}>Procesar pago</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
