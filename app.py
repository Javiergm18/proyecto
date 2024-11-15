import os
from flask import Flask, request, jsonify
from jnius import autoclass

app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Configuración de la conexión con el servidor RMI en Java
System = autoclass('java.lang.System')
System.setProperty('java.rmi.server.hostname', 'localhost')  # Cambia 'localhost' si tu servidor está en otra máquina
RMIInterface = autoclass('java.rmi.Naming')

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({"error": "No file provided"}), 400

    file = request.files['file']
    if not file.filename.endswith('.java'):
        return jsonify({"error": "Only .java files are allowed"}), 400

    # Guardar el archivo
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
    file.save(file_path)

    try:
        # Conectar al servidor RMI
        rmi_server = RMIInterface.lookup("//localhost/RMIInterface")  # Cambia 'localhost' por la IP del servidor Java

        # Llamar al método remoto 'processFile'
        result = rmi_server.processFile(file_path)
        return jsonify({"result": result})

    except Exception as e:
        return jsonify({"error": f"Error connecting to RMI server: {str(e)}"}), 500

if __name__ == '__main__':
    app.run(debug=True)
