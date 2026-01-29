<!DOCTYPE html>
<html>
<head>
    <title>Editar Medicamento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center" style="height: 100vh;">

    <div class="card p-4" style="width: 400px;">
        <h3>Editar: {{ $medication->name }}</h3>
        
        <form action="{{ route('medications.update', $medication->id) }}" method="POST">
            @csrf @method('PUT')
            
            <div class="mb-2">
                <label>Nome</label>
                <input type="text" name="name" value="{{ $medication->name }}" class="form-control" required>
            </div>
            <div class="mb-2">
                <label>Dosagem</label>
                <input type="text" name="dosage" value="{{ $medication->dosage }}" class="form-control" required>
            </div>
            <div class="mb-2">
                <label>Forma</label>
                <input type="text" name="form" value="{{ $medication->form }}" class="form-control">
            </div>
            <div class="mb-2">
                <label>Bula</label>
                <input type="text" name="leaflet" value="{{ $medication->leaflet }}" class="form-control">
            </div>

            <button type="submit" class="btn btn-primary w-100 mb-2">Guardar Alterações</button>
            <a href="{{ route('dashboard') }}" class="btn btn-secondary w-100">Cancelar</a>
        </form>
    </div>

</body>
</html>