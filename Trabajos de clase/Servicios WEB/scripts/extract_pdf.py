import sys
from pathlib import Path
try:
    from PyPDF2 import PdfReader
except Exception as e:
    print('MISSING_LIB')
    sys.exit(2)

if len(sys.argv) < 2:
    print('Usage: python extract_pdf.py <pdf_path>')
    sys.exit(1)

pdf_path = Path(sys.argv[1])
if not pdf_path.exists():
    print(f'NOT_FOUND:{pdf_path}')
    sys.exit(3)

reader = PdfReader(str(pdf_path))
text_parts = []
for page in reader.pages:
    try:
        text_parts.append(page.extract_text() or '')
    except Exception:
        text_parts.append('')

text = '\n\n'.join(text_parts)
print(text)
