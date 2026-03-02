import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '../../store/useAuthStore';
import api from '../../api/client';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts';
import { Package, TrendingUp, Users, DollarSign } from 'lucide-react';

const mockChartData = [
    { name: 'Seg', vendas: 4000 },
    { name: 'Ter', vendas: 3000 },
    { name: 'Qua', vendas: 5000 },
    { name: 'Qui', vendas: 2780 },
    { name: 'Sex', vendas: 8900 },
    { name: 'Sab', vendas: 2390 },
    { name: 'Dom', vendas: 3490 },
];

export const Dashboard = () => {
    const { user } = useAuthStore();

    const { data: stats, isLoading } = useQuery({
        queryKey: ['dashboard-stats'],
        queryFn: async () => {
            // Placeholder data until we have the real metrics endpoint
            return {
                totalVendas: 154200.50,
                pedidosPendentes: 12,
                clientesAtivos: 45,
                produtosEstoque: 128
            };
        },
    });

    if (isLoading) return <div className="skeleton" style={{ height: '300px' }} />;

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1 className="page-title">Dashboard</h1>
                    <p className="page-subtitle">Resumo das atividades da sua revenda</p>
                </div>
                <button className="btn btn-primary">
                    <TrendingUp size={16} />
                    Gerar Relatório
                </button>
            </div>

            <div className="stats-grid">
                <div className="stat-card">
                    <div className="flex justify-between items-center text-muted">
                        <span className="stat-label">Faturamento (Mês)</span>
                        <DollarSign size={20} />
                    </div>
                    <div className="stat-value">
                        {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(stats?.totalVendas || 0)}
                    </div>
                </div>

                <div className="stat-card">
                    <div className="flex justify-between items-center text-muted">
                        <span className="stat-label">Pedidos Pendentes</span>
                        <Package size={20} />
                    </div>
                    <div className="stat-value text-warning">{stats?.pedidosPendentes}</div>
                </div>

                <div className="stat-card">
                    <div className="flex justify-between items-center text-muted">
                        <span className="stat-label">Clientes Ativos</span>
                        <Users size={20} />
                    </div>
                    <div className="stat-value">{stats?.clientesAtivos}</div>
                </div>

                <div className="stat-card">
                    <div className="flex justify-between items-center text-muted">
                        <span className="stat-label">Produtos Ativos</span>
                        <Package size={20} />
                    </div>
                    <div className="stat-value">{stats?.produtosEstoque}</div>
                </div>
            </div>

            <div className="card">
                <div className="card-header">
                    <h2 className="card-title">Volume de Vendas (Últimos 7 dias)</h2>
                </div>
                <div style={{ width: '100%', height: 350 }}>
                    <ResponsiveContainer>
                        <LineChart data={mockChartData}>
                            <CartesianGrid strokeDasharray="3 3" stroke="#2d3f32" vertical={false} />
                            <XAxis dataKey="name" stroke="#6b7c72" axisLine={false} tickLine={false} />
                            <YAxis
                                stroke="#6b7c72"
                                axisLine={false}
                                tickLine={false}
                                tickFormatter={(value) => `R$ ${value / 1000}k`}
                            />
                            <Tooltip
                                contentStyle={{ backgroundColor: '#111b15', borderColor: 'rgba(74,222,128,0.2)' }}
                                itemStyle={{ color: '#4ade80' }}
                            />
                            <Line
                                type="monotone"
                                dataKey="vendas"
                                stroke="#22c55e"
                                strokeWidth={3}
                                dot={{ fill: '#111b15', stroke: '#22c55e', strokeWidth: 2, r: 4 }}
                                activeDot={{ r: 6, fill: '#4ade80', stroke: '#14532d' }}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
};
