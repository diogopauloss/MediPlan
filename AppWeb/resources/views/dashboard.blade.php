@extends('layout')

@section('content')
    <div class="row">    
        <div class="col-12">
            <h3 class="text-primary mb-3">👥 Gestão de Utilizadores</h3>
            <p class="text-muted">Aqui podes decidir quem tem acesso a este site (Admin Web) ou apenas à App.</p>
            
            <div class="card p-4 shadow-sm bg-white">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Estado Atual</th>
                            <th>Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($users as $user)
                        <tr>
                            <td>{{ $user->id }}</td>
                            <td>{{ $user->name }}</td>
                            <td>{{ $user->email }}</td>
                            <td>
                                @if($user->id_web == 1)
                                    <span class="badge bg-success">Admin Web</span>
                                @else
                                    <span class="badge bg-secondary">Apenas App</span>
                                @endif
                            </td>
                            <td>
                                @if(Auth::id() != $user->id)
                                    <form action="{{ route('users.toggle', $user->id) }}" method="POST">
                                        @csrf
                                        @if($user->id_web == 1)
                                            <button class="btn btn-sm btn-outline-danger">Remover Acesso</button>
                                        @else
                                            <button class="btn btn-sm btn-outline-success">Tornar Admin</button>
                                        @endif
                                    </form>
                                @else
                                    <span class="text-muted small">(Tu)</span>
                                @endif
                            </td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        </div>
    </div>
@endsection