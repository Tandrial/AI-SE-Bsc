# Basierend auf einem Makefile von Dr. Thomas Dreibholz

all: optimized

clean:
	rm -f *.toc *.log *.idx *.ind *.aux *.loa *.out  *.dvi *.bak *.bbl *.blg *.glo *.nlo *.ilg *.lof *.lot *.gls *.nls *.brf *.dvi *.ps

pdf: clean
	pdflatex -interaction batchmode krane.tex
	bibtex -terse krane
	pdflatex -interaction batchmode krane.tex
	pdflatex -interaction batchmode krane.tex

optimized:	pdf
	pdfopt krane.pdf krane-optimized.pdf
