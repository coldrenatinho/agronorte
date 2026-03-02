import { useState } from 'react';
import { Search, Eye, CheckCircle, Truck, XCircle } from 'lucide-react';

const mockOrders = [
    { id: 'ORD-2024-001', buyer: 'Fazenda Rio Grande', date: '02/03/2026', total: 14500.00, status: 'PENDING' },
    { id: 'ORD-2024-002', buyer: 'Agropecuária Silva', date: '01/03/2026', total: 4200.50, status: 'CONFIRMED' },
    { id: 'ORD-2024-003', buyer: 'Sítio Novo Horizonte', date: '28/02/2026', total: 1890.00, status: 'SHIPPED' },
    { id: 'ORD-2024-004', buyer: 'Cooperativa MT Sul', date: '25/02/2026', total: 56000.00, status: 'DELIVERED' },
    { id: 'ORD-2024-005', buyer: 'Fazenda Esperança', date: '20/02/2026', total: 320.00, status: 'CANCELLED' },
];

const StatusBadge = ({ status }: { status: string }) => {
    switch (status) {
        case 'PENDING': return <span className="badge badge-warning">Pendente</span>;
        case 'CONFIRMED': return <span className="badge badge-info">Confirmado</span>;
        case 'SHIPPED': return <span className="badge badge-success">Enviado</span>;
        case 'DELIVERED': return <span className="badge badge-neutral">Entregue</span>;
        case 'CANCELLED': return <span className="badge badge-error">Cancelado</span>;
        default: return null;
    }
};

export const Orders = () => {
    const [searchTerm, setSearchTerm] = useState('');

    const filteredOrders = mockOrders.filter(o =>
        o.buyer.toLowerCase().includes(searchTerm.toLowerCase()) ||
        o.id.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1 className="page-title">Gestão de Pedidos</h1>
                    <p className="page-subtitle">Acompanhe as compras dos seus clientes</p>
                </div>
            </div>

            <div className="card">
                <div className="card-header">
                    <div className="search-bar" style={{ width: '350px' }}>
                        <Search size={18} className="text-muted" />
                        <input
                            type="text"
                            placeholder="Buscar por cliente ou ID do pedido..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                </div>

                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>ID do Pedido</th>
                                <th>Cliente</th>
                                <th>Data</th>
                                <th>Valor Total</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredOrders.map((order) => (
                                <tr key={order.id}>
                                    <td className="font-bold text-muted">{order.id}</td>
                                    <td>{order.buyer}</td>
                                    <td>{order.date}</td>
                                    <td className="font-bold">
                                        {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(order.total)}
                                    </td>
                                    <td><StatusBadge status={order.status} /></td>
                                    <td>
                                        <div className="flex gap-2">
                                            <button className="btn btn-secondary btn-sm" title="Ver detalhes">
                                                <Eye size={14} />
                                            </button>
                                            {order.status === 'PENDING' && (
                                                <button className="btn btn-primary btn-sm" title="Confirmar Pedido">
                                                    <CheckCircle size={14} />
                                                </button>
                                            )}
                                            {order.status === 'CONFIRMED' && (
                                                <button className="btn btn-primary btn-sm" title="Marcar como Enviado">
                                                    <Truck size={14} />
                                                </button>
                                            )}
                                            {order.status === 'PENDING' && (
                                                <button className="btn btn-danger btn-sm" title="Cancelar Pedido">
                                                    <XCircle size={14} />
                                                </button>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};
