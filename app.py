import socket
from flask import Flask, request, render_template, jsonify
import os

app = Flask(__name__)

# Ruta donde se guardarán los archivos cargados
UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Configuración del servidor TCP
TCP_SERVER_IP = '127.0.0.1'  # Cambia esto si el servidor Java está en otra máquina
TCP_SERVER_PORT = 5050

# Función para enviar archivos al servidor TCP
def send_file_to_tcp_server(file_path):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            print(f"Intentando conectar al servidor TCP en {TCP_SERVER_IP}:{TCP_SERVER_PORT}...")
            sock.connect((TCP_SERVER_IP, TCP_SERVER_PORT))
            print(f"Conexión establecida con éxito.")

            print(f"Enviando archivo: {file_path}")
            sock.sendall(file_path.encode('utf-8'))

            response = b""
            while True:
                print("Esperando respuesta del servidor...")
                chunk = sock.recv(4096)
                if not chunk:
                    print("No hay más datos del servidor. Finalizando recepción.")
                    break
                response += chunk
            response = response.decode('utf-8')
            print(f"Respuesta recibida del servidor: {response}")
            return response
    except Exception as e:
        print(f"Error durante la conexión TCP: {str(e)}")
        return f"Error durante la conexión TCP: {str(e)}"

# Página de inicio con el formulario de carga
@app.route('/')
def index():
    return render_template('index.html')

# Ruta para manejar la carga de archivos
@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({"error": "No se ha enviado ningún archivo."}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "No se ha seleccionado ningún archivo."}), 400

    # Guardar el archivo en la carpeta de uploads
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
    file.save(file_path)

    print(f"Archivo guardado: {file_path}")  # Depuración

    # Enviar el archivo al servidor TCP
    result = send_file_to_tcp_server(file_path)

    if not result.strip():  # Si la respuesta está vacía
        print("Respuesta vacía del servidor.")  # Depuración
        return jsonify({"error": "No se recibió respuesta del servidor."}), 500

    print(f"Resultado procesado: {result}")  # Depuración
    return jsonify({"result": result})

if __name__ == '__main__':
    app.run(debug=True)
