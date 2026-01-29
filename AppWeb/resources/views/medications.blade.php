@extends('layout')

@section('content')
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="text-primary">💊 Gestão de Medicamentos</h3>
    </div>

    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card p-3 shadow-sm">
                <h5 class="mb-3">Novo Medicamento</h5>
                <form action="{{ route('medications.store') }}" method="POST">
                    @csrf
                    <div class="mb-2">
                        <label class="form-label text-muted small">Nome</label>
                        <input type="text" name="name" placeholder="Ex: Ben-u-ron" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label text-muted small">Dosagem</label>
                        <input type="text" name="dosage" placeholder="Ex: 500mg" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label text-muted small">Forma</label>
                        <input type="text" name="form" placeholder="Ex: Comprimido" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted small">Bula (Link ou texto)</label>
                        <input type="text" name="leaflet" placeholder="Opcional" class="form-control">
                    </div>
                    <button type="submit" class="btn btn-success w-100">Adicionar Medicamento</button>
                </form>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card p-3 shadow-sm">
                <table class="table table-hover align-middle">
                    <thead class="table-secondary">
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Dosagem</th>
                            <th class="text-end">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($medications as $med)
                        <tr>
                            <td>{{ $med->id }}</td>
                            <td><strong>{{ $med->name }}</strong></td>
                            <td>{{ $med->dosage }}</td>
                            <td class="text-end">
                                <a href="{{ route('medications.edit', $med->id) }}" class="btn btn-sm btn-outline-primary">Editar</a>
                                
                                <form action="{{ route('medications.delete', $med->id) }}" method="POST" style="display:inline;">
                                    @csrf @method('DELETE')
                                    <button class="btn btn-sm btn-outline-danger" onclick="return confirm('Tem a certeza que quer apagar {{ $med->name }}?')">X</button>
                                </form>
                            </td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        </div>
    </div>
@endsection