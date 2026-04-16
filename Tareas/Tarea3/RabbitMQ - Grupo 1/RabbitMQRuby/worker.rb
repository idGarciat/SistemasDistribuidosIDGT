require 'bunny'

connection = Bunny.new(hostname: 'localhost')
connection.start

channel = connection.create_channel
queue = channel.queue('task_queue', durable: true)

# SORTEO JUSTO: No dar más de 1 tarea a un trabajador a la vez
channel.prefetch(1)

puts " [*] Esperando tareas. Para salir presiona CTRL+C"

begin
  # manual_ack: true significa que nosotros avisamos cuando terminamos
  # block: true hace que el programa se quede corriendo infinitamente
  queue.subscribe(manual_ack: true, block: true) do |delivery_info, _properties, body|
    puts " [x] Recibida: '#{body}'"

    # Simular trabajo pesado: pausar 1 segundo por cada punto en el mensaje
    sleep_time = body.count('.')
    sleep(sleep_time)

    puts " [x] Terminado"

    # Avisarle a RabbitMQ que la tarea se completó exitosamente
    channel.ack(delivery_info.delivery_tag)
  end
rescue Interrupt => _
  # Cierra la conexión limpiamente si presionas CTRL+C
  connection.close
  exit(0)
end