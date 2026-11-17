import http.server
import socketserver
import os
import sys

PORT = 4200
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DIRECTORY = os.path.join(BASE_DIR, "dist")

class SPALocalHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        # Resolve requested file path
        path = self.translate_path(self.path)
        if not os.path.exists(path) or os.path.isdir(path):
            if not os.path.exists(os.path.join(path, "index.html")):
                self.path = "/index.html"
        return super().do_GET()

# Allow address reuse to avoid port bind lock
socketserver.TCPServer.allow_reuse_address = True

with socketserver.TCPServer(("", PORT), SPALocalHandler) as httpd:
    print(f"Insurance Management System SPA running at http://localhost:{PORT}")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped.")
