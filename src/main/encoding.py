import os
import chardet
import argparse

def detect_encoding(filename):
    with open(filename, 'rb') as file:
        raw_data = file.read()
    return chardet.detect(raw_data)['encoding']

def convert_to_utf8(filename):
    encoding = detect_encoding(filename)
    
    if encoding != 'utf-8':
        with open(filename, 'r', encoding=encoding) as file:
            content = file.read()

        with open(filename, 'w', encoding='utf-8') as file:
            file.write(content)
            
        print(f"Le fichier {filename} a été converti de {encoding} en UTF-8.")
        
        
    else:
        print(f"Le fichier {filename} est déjà en UTF-8.")

def convert_directory_to_utf8(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            file_path = os.path.join(root, file)
            try:
                convert_to_utf8(file_path)
            except Exception as e:
                print(f"Erreur lors de la conversion du fichier {file_path}: {e}")

def main():
    parser = argparse.ArgumentParser(description="Convertir des fichiers ou des dossiers entiers en UTF-8.")
    parser.add_argument('path', help="Chemin vers le fichier ou le dossier à convertir.")
    args = parser.parse_args()

    if os.path.isdir(args.path):
        convert_directory_to_utf8(args.path)
    elif os.path.isfile(args.path):
        convert_to_utf8(args.path)
    else:
        print("Le chemin fourni n'est ni un fichier ni un dossier.")

if __name__ == "__main__":
    main()