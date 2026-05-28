<?php

namespace App\Http\Controllers;

use Throwable;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use App\Services\IntermediadorService;

class IntermediadorController extends Controller
{
    public function __construct(
        private readonly IntermediadorService $intermediadorService,
    ) {
    }

    public function login(Request $request): JsonResponse
    {
        $data = $request->validate([
            'usuario' => ['nullable', 'string'],
            'password' => ['nullable', 'string'],
        ]);

        return response()->json($this->intermediadorService->issueToken($data['usuario'] ?? null));
    }

    public function transaccion(Request $request): JsonResponse
    {
        if (! $this->intermediadorService->authorizeBearer($request->header('Authorization'))) {
            return response()->json([
                'error' => 'Unauthorized',
                'message' => 'Falta el token Bearer en Authorization.',
            ], 401);
        }

        $data = $request->validate([
            'fecha' => ['required', 'date'],
            'cuentaOrigen' => ['required', 'string'],
            'cuentaDestino' => ['required', 'string'],
            'monto' => ['required', 'numeric', 'min:0.01'],
        ]);

        try {
            return response()->json($this->intermediadorService->registerTransaction($data));
        } catch (Throwable $exception) {
            $status = (int) $exception->getCode();

            if ($status < 400 || $status > 599) {
                $status = 500;
            }

            return response()->json([
                'error' => 'transaction_failed',
                'message' => $exception->getMessage(),
            ], $status);
        }
    }
}
