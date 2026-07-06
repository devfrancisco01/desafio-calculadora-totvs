import { useState } from 'react'
import './App.css'

type ParcelaResponse = {
  dataCompetencia: string;
  valorEmprestimo: number;
  saldoDevedor: number;
  consolidada: string;
  parcelaTotal: number;
  amortizacaoPrincipal: number;
  saldo: number; 
  jurosProvisao: number;
  jurosAcumulado: number;
  jurosPago: number;
}

function App() {
  const [dataInicial, setDataInicial] = useState('');
  const [dataFinal, setDataFinal] = useState('');
  const [primeiroPagamento, setPrimeiroPagamento] = useState('');
  const [valorEmprestimo, setValorEmprestimo] = useState<number | ''>('');
  const [taxaJuros, setTaxaJuros] = useState<number | ''>('');
  
  const [resultados, setResultados] = useState<ParcelaResponse[]>([]);
  const [erro, setErro] = useState('');

  const isValido = () => {
    if (!dataInicial || !dataFinal || !primeiroPagamento || valorEmprestimo === '' || taxaJuros === '') return false;
    
    const dInicial = new Date(dataInicial);
    const dFinal = new Date(dataFinal);
    const dPagamento = new Date(primeiroPagamento);

    if (dFinal <= dInicial) return false;
    if (dPagamento <= dInicial || dPagamento >= dFinal) return false;

    return true;
  };

  const calcular = async () => {
    setErro('');
    try {
      const response = await fetch('http://localhost:8080/api/calculadora/calcular', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          dataInicial,
          dataFinal,
          primeiroPagamento,
          valorEmprestimo,
          taxaJuros: Number(taxaJuros) / 100
        })
      });

      if (!response.ok) throw new Error('Erro na requisição. Verifique os dados e a conexão com a API.');

      const data = await response.json();
      setResultados(data);
    } catch (error) {
      setErro('Não foi possível conectar com o servidor.');
    }
  };

  // Formatters
  const formatarMoeda = (valor: number) => {
    return new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(valor);
  };

  const formatarData = (dataStr: string) => {
    if (!dataStr) return '';
    const [ano, mes, dia] = dataStr.split('-');
    return `${dia}/${mes}/${ano}`;
  };

  return (
    <div className="container">
      <h2>Calculadora de Empréstimos</h2>

      <div className="form-grid">
        <div className="input-group">
          <label>Data Inicial</label>
          <input type="date" value={dataInicial} onChange={e => setDataInicial(e.target.value)} />
        </div>
        <div className="input-group">
          <label>Data Final</label>
          <input type="date" value={dataFinal} onChange={e => setDataFinal(e.target.value)} />
        </div>
        <div className="input-group">
          <label>Primeiro Pagamento</label>
          <input type="date" value={primeiroPagamento} onChange={e => setPrimeiroPagamento(e.target.value)} />
        </div>
        <div className="input-group">
          <label>Valor de Empréstimo</label>
          <input 
            type="number" 
            value={valorEmprestimo} 
            onChange={e => setValorEmprestimo(e.target.value === '' ? '' : Number(e.target.value))} 
          />
        </div>
        <div className="input-group">
          <label>Taxa de Juros (%)</label>
          <input 
            type="number" 
            value={taxaJuros} 
            onChange={e => setTaxaJuros(e.target.value === '' ? '' : Number(e.target.value))} 
          />
        </div>
      </div>

      <button onClick={calcular} disabled={!isValido()}>
        Calcular
      </button>

      {erro && <p className="error">{erro}</p>}

      {resultados.length > 0 && (
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Data Competência</th>
                <th>Valor de Empréstimo</th>
                <th>Saldo Devedor</th>
                <th>Consolidada</th>
                <th>Parcela Total</th>
                <th>Amortização Principal</th>
                <th>Saldo</th> 
                <th>Juros Provisão</th>
                <th>Juros Acumulado</th>
                <th>Juros Pago</th>
              </tr>
            </thead>
            <tbody>
              {resultados.map((row, index) => (
                <tr key={index}>
                  <td className="center">{formatarData(row.dataCompetencia)}</td>
                  <td>{formatarMoeda(row.valorEmprestimo)}</td>
                  <td>{formatarMoeda(row.saldoDevedor)}</td>
                  <td className="center">{row.consolidada}</td>
                  <td>{formatarMoeda(row.parcelaTotal)}</td>
                  <td>{formatarMoeda(row.amortizacaoPrincipal)}</td>
                  <td>{formatarMoeda(row.saldo)}</td> 
                  <td>{formatarMoeda(row.jurosProvisao)}</td>
                  <td>{formatarMoeda(row.jurosAcumulado)}</td>
                  <td>{formatarMoeda(row.jurosPago)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

export default App