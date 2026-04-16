require 'bunny'

# Conectarse a RabbitMQ
connection = Bunny.new(hostname: 'localhost')
connection.start

channel = connection.create_channel
# Declarar la cola como durable (no se borra al reiniciar)
queue = channel.queue('task_queue', durable: true)

puts "Enviando 5 tareas a la cola..."

(1..5).each do |i|
  # Creamos el mensaje con puntos que representan segundos
  dots = "." * i
  message = "Tarea #{i} #{dots}"

  # Enviar el mensaje marcándolo como persistente
  queue.publish(message, persistent: true)
  puts " [x] Enviado: '#{message}'"
end

connection.close