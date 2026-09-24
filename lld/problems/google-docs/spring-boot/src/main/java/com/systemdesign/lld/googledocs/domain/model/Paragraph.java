package com.systemdesign.lld.googledocs.domain.model;

import com.systemdesign.lld.googledocs.domain.visitor.DocumentVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paragraph implements DocumentElement {

    private final List<TextRun> runs = new ArrayList<>();

    public Paragraph() {}

    public Paragraph(String initialText, TextStyle style) {
        if (initialText != null && !initialText.isEmpty()) {
            runs.add(new TextRun(initialText, style));
        }
    }

    public List<TextRun> getRuns() {
        return Collections.unmodifiableList(runs);
    }

    @Override
    public int getLength() {
        int len = 0;
        for (TextRun run : runs) {
            len += run.getLength();
        }
        return len;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (TextRun run : runs) {
            sb.append(run.getText());
        }
        return sb.toString();
    }

    public void insert(int position, String text, TextStyle style) {
        int totalLen = getLength();
        if (position < 0 || position > totalLen) {
            throw new IndexOutOfBoundsException("Position " + position + " out of bounds");
        }
        if (text == null || text.isEmpty()) {
            return;
        }

        TextRun newRun = new TextRun(text, style != null ? style : TextStyle.DEFAULT);

        if (runs.isEmpty() || position == totalLen) {
            runs.add(newRun);
            return;
        }

        int currentOffset = 0;
        for (int i = 0; i < runs.size(); i++) {
            TextRun run = runs.get(i);
            int runLen = run.getLength();

            if (position == currentOffset) {
                runs.add(i, newRun);
                return;
            } else if (position > currentOffset && position < currentOffset + runLen) {
                int splitIndex = position - currentOffset;
                String left = run.getText().substring(0, splitIndex);
                String right = run.getText().substring(splitIndex);

                run.setText(left);
                runs.add(i + 1, newRun);
                runs.add(i + 2, new TextRun(right, run.getStyle()));
                return;
            }
            currentOffset += runLen;
        }
    }

    public List<TextRun> delete(int start, int length) {
        int totalLen = getLength();
        if (start < 0 || start + length > totalLen || length < 0) {
            throw new IndexOutOfBoundsException("Delete range out of bounds");
        }
        if (length == 0) {
            return Collections.emptyList();
        }

        int end = start + length;
        List<TextRun> deletedRuns = new ArrayList<>();
        List<TextRun> newRuns = new ArrayList<>();

        int currentOffset = 0;
        for (TextRun run : runs) {
            int runStart = currentOffset;
            int runEnd = currentOffset + run.getLength();

            if (runEnd <= start || runStart >= end) {
                newRuns.add(run);
            } else {
                int overlapStart = Math.max(runStart, start);
                int overlapEnd = Math.min(runEnd, end);

                int relStart = overlapStart - runStart;
                int relEnd = overlapEnd - runStart;

                deletedRuns.add(new TextRun(run.getText().substring(relStart, relEnd), run.getStyle()));

                String left = run.getText().substring(0, relStart);
                String right = run.getText().substring(relEnd);

                if (!left.isEmpty()) {
                    newRuns.add(new TextRun(left, run.getStyle()));
                }
                if (!right.isEmpty()) {
                    newRuns.add(new TextRun(right, run.getStyle()));
                }
            }
            currentOffset = runEnd;
        }

        runs.clear();
        runs.addAll(newRuns);
        return deletedRuns;
    }

    public void applyStyle(int start, int length, TextStyle newStyle) {
        List<TextRun> deleted = delete(start, length);
        int currentPos = start;
        for (TextRun del : deleted) {
            insert(currentPos, del.getText(), newStyle);
            currentPos += del.getLength();
        }
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
        for (TextRun run : runs) {
            run.accept(visitor);
        }
    }
}
