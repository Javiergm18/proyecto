from flask import Flask, request, render_template, jsonify
import socket
import os

app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

TCP_SERVER_IP = '127.0.0.1'
TCP_SERVER_PORT = 5050

def send_file_to_tcp_server(file_path):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.connect((TCP_SERVER_IP, TCP_SERVER_PORT))
            sock.sendall(file_path.encode('utf-8'))
            response = b""
            while True:
                chunk = sock.recv(4096)
                if not chunk:
                    break
                response += chunk
            return response.decode('utf-8')
    except Exception as e:
        return f"Error: {str(e)}"

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_file():
    file = request.files['file']
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
    file.save(file_path)
    result = send_file_to_tcp_server(file_path)
    return jsonify({"result": result})

if __name__ == '__main__':
    app.run(debug=True)
