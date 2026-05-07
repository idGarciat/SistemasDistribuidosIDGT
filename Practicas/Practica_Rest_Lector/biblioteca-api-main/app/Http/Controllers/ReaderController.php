<?php

namespace App\Http\Controllers;

use App\Models\Reader;
use Illuminate\Http\Request;

class ReaderController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $readers = Reader::all();
        return response()->json($readers);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $input = $request->all();
        $reader = Reader::create($input);
        return response()->json($reader, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(Reader $reader)
    {
        return response()->json($reader);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Reader $reader)
    {
        $input = $request->all();
        $reader->update($input);
        return response()->json($reader, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Reader $reader)
    {
        $reader->delete();
        return response()->json($reader, 204);
    }
}
