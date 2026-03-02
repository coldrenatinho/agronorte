import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Search, Filter, Plus } from 'lucide-react';

export const Products = () => {
    const [searchTerm, setSearchTerm] = useState('');

    const { data, isLoading } = useQuery({
        queryKey: ['products'],
        queryFn: async () => {
            // Em vez de bater na API que ainda não está rodando, vamos mockar os dados 
            // para visualização do SaaS. O React Query gerenciará o cache real.
            return {
                content: [
                    { id: '1', name: 'Semente de Soja Brasmax', category: 'SEMENTES', price: 250.0, stockQuantity: 500 },
                    { id: '2', name: 'Fertilizante NPK 10-10-10', category: 'INSUMOS', price: 180.5, stockQuantity: 1200 },
                    { id: '3', name: 'Trator CBT 2105 (Peça de reposição)', category: 'MAQUINARIO', price: 12500.0, stockQuantity: 3 },
                    { id: '4', name: 'Herbicida Roundup', category: 'INSUMOS', price: 420.0, stockQuantity: 50 },
                    { id: '5', name: 'Milho Safrinha Pioneer', category: 'SEMENTES', price: 195.0, stockQuantity: 800 },
                ]
            };
        },
    });

    const products = data?.content || [];
    const filteredProducts = products.filter(p =>
        p.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1 className="page-title">Catálogo de Produtos</h1>
                    <p className="page-subtitle">Gerencie seu estoque e preços</p>
                </div>
                <button className="btn btn-primary">
                    <Plus size={16} />
                    Novo Produto
                </button>
            </div>

            <div className="card">
                <div className="card-header">
                    <div className="search-bar" style={{ width: '300px' }}>
                        <Search size={18} className="text-muted" />
                        <input
                            type="text"
                            placeholder="Buscar pelo nome..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                    <button className="btn btn-secondary btn-sm">
                        <Filter size={16} /> Filtros
                    </button>
                </div>

                {isLoading ? (
                    <div className="skeleton" style={{ height: '400px' }} />
                ) : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Produto</th>
                                    <th>Categoria</th>
                                    <th>Preço (R$)</th>
                                    <th>Estoque</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredProducts.map((product) => (
                                    <tr key={product.id}>
                                        <td className="font-bold">{product.name}</td>
                                        <td><span className="badge badge-neutral">{product.category}</span></td>
                                        <td className="text-primary-400 font-bold">
                                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
                                        </td>
                                        <td>
                                            <span className={product.stockQuantity < 100 ? 'text-error font-bold' : ''}>
                                                {product.stockQuantity} un
                                            </span>
                                        </td>
                                        <td><span className="badge badge-success">Ativo</span></td>
                                        <td>
                                            <button className="btn btn-secondary btn-sm">Editar</button>
                                        </td>
                                    </tr>
                                ))}

                                {filteredProducts.length === 0 && (
                                    <tr>
                                        <td colSpan={6} className="text-center text-muted" style={{ padding: '30px' }}>
                                            Nenhum produto encontrado.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};
