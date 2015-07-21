# Basierend auf einem Makefile von Dr. Thomas Dreibholz

all:   optimized

clean:
	rm -f *.toc *.log *.idx *.ind *.aux *.loa *.out *~ *.dvi *.bak *.bbl *.blg *.glo *.nlo *.ilg *.lof *.lot *.gls *.nls *.brf

distclean: clean

really-clean: clean
	rm -f *.dvi *.ps *.pdf

pdf: clean
	pdflatex -interaction batchmode krane-bathesis.tex
	bibtex -terse krane-bathesis
	pdflatex -interaction batchmode krane-bathesis.tex
	pdflatex -interaction batchmode krane-bathesis.tex

optimized:	pdf
	pdfopt krane-bathesis.pdf krane-bathesis-optimized.pdf
