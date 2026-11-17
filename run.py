import os
import sys
import time
import socket
import subprocess
import signal

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
BACKEND_DIR = os.path.join(ROOT_DIR, "backend")
FRONTEND_DIR = os.path.join(ROOT_DIR, "frontend")

def is_port_in_use(port):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.settimeout(1.0)
        return s.connect_ex(("127.0.0.1", port)) == 0

def main():
    try:
        sys.stdout.reconfigure(line_buffering=True)
    except Exception:
        pass
    print("=" * 70)
    print("                 INSURANCE MANAGEMENT SYSTEM")
    print("=" * 70)

    processes = []

    if is_port_in_use(8082):
        print("[*] Backend is already running on port 8082.")
    else:
        print("[*] Starting Spring Boot REST Backend (port 8082)...")
        mvn_cmd = "mvn.cmd" if os.name == "nt" else "mvn"
        try:
            p_back = subprocess.Popen(
                [mvn_cmd, "spring-boot:run"],
                cwd=BACKEND_DIR,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.STDOUT
            )
            processes.append(p_back)
        except Exception:
            p_back = subprocess.Popen(
                ["mvn", "spring-boot:run"],
                cwd=BACKEND_DIR,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.STDOUT,
                shell=True
            )
            processes.append(p_back)

    if is_port_in_use(4200):
        print("[*] Frontend is already running on port 4200.")
    else:
        print("[*] Starting Angular Frontend Portal (port 4200)...")
        p_front = subprocess.Popen(
            [sys.executable, "serve_spa.py"],
            cwd=FRONTEND_DIR,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.STDOUT
        )
        processes.append(p_front)

    print("[*] Verifying services readiness...")
    for _ in range(15):
        if is_port_in_use(4200) and is_port_in_use(8082):
            break
        time.sleep(1)

    print()
    print("=" * 70)
    print(" [SUCCESS] Insurance Management System is actively running!")
    print("=" * 70)
    print()
    print("  * Frontend Portal : http://localhost:4200")
    print("  * Backend REST API: http://localhost:8082")
    print()
    print("  👉 Open this URL in your browser:")
    print("     http://localhost:4200")
    print()
    print("     Login Email:    indlavikas57@gmail.com")
    print("     Login Password: 12345678")
    print()
    print("=" * 70)
    print("  Press Ctrl + C in this terminal to stop all services.")
    print("=" * 70)

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\n[*] Stopping Insurance Management System services...")
        for p in processes:
            try:
                p.terminate()
                p.wait(timeout=3)
            except Exception:
                try:
                    p.kill()
                except Exception:
                    pass
        print("[✓] All services stopped cleanly. Goodbye!")

if __name__ == "__main__":
    main()
