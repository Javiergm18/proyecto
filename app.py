from flask import Flask, request, render_template, jsonify
import os
import Pyro4

app = Flask(__name__)

UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


@Pyro4.expose
class RMIServerClient:
    def __init__(self):

        self.rmi_interface = Pyro4.Proxy("PYRONAME:RMIInterface@localhost:1099")
    
    def process_file(self, file_path):
        try:

            result = self.rmi_interface.processFile(file_path)
            return result
        except Exception as e:
            return f"Error en el procesamiento del archivo: {str(e)}"



@app.route('/')
def index():
    return render_template('index.html')


@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({"error": "No se ha enviado ningún archivo."}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "No se ha seleccionado ningún archivo."}), 400

    file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
    file.save(file_path)


    rmi_client = RMIServerClient()

    result = rmi_client.process_file(file_path)

    return jsonify({"result": result})

if __name__ == '__main__':
    app.run(debug=True)
