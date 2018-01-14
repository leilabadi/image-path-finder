package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.CellType;
import fun.leilabadi.pathfinder.HeuristicPathFinder;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.common.UiMode;
import fun.leilabadi.pathfinder.map.*;
import fun.leilabadi.pathfinder.ui.components.InteractiveGridImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditablePathFinderFrame extends BasePathFinderFrame implements ActionListener {
    private final int rowCount;
    private final int columnCount;
    private final Size size;
    private final JButton buttonRun;
    private final JButton buttonReset;
    private final JButton buttonSave;

    public EditablePathFinderFrame(int rowCount, int columnCount) {
        super("Editable Path Finder");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.size = new Size(rowCount, columnCount);

        grid = new InteractiveGridImpl(size.getRowCount(), size.getColumnCount(), UiMode.EDIT);

        buttonRun = new JButton("Run");
        buttonRun.addActionListener(this);

        buttonReset = new JButton("Reset");
        buttonReset.addActionListener(this);

        buttonSave = new JButton("Save");
        buttonSave.addActionListener(this);

        final JButton buttonEmpty = new JButton("Empty");
        buttonEmpty.addActionListener(this);
        buttonEmpty.setActionCommand(CellType.EMPTY.toString());

        final JButton buttonObstacle = new JButton("Obstacle");
        buttonObstacle.addActionListener(this);
        buttonObstacle.setActionCommand(CellType.OBSTACLE.toString());

        final JButton buttonStart = new JButton("Start");
        buttonStart.addActionListener(this);
        buttonStart.setActionCommand(CellType.START.toString());

        final JButton buttonGoal = new JButton("Goal");
        buttonGoal.addActionListener(this);
        buttonGoal.setActionCommand(CellType.GOAL.toString());

        final JPanel panelBottom = new JPanel();
        panelBottom.add(buttonRun);
        panelBottom.add(buttonReset);
        panelBottom.add(buttonSave);
        panelBottom.add(buttonEmpty);
        panelBottom.add(buttonObstacle);
        panelBottom.add(buttonStart);
        panelBottom.add(buttonGoal);

        setProperSize(size);
        setLayout(new BorderLayout());
        add((JComponent) grid, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        setVisible(true);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                onWindowClosing();
            }
        });

        init();
    }

    private void init() {
        super.reset();

        MapBuilder builder = new EmptyMapBuilder(rowCount, columnCount);
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        pathFinder = new HeuristicPathFinder(map);

        int[][] data = new int[size.getRowCount()][size.getColumnCount()];
        for (int y = 0; y < size.getRowCount(); y++) {
            for (int x = 0; x < size.getColumnCount(); x++) {
                data[y][x] = map.getCell(y, x).getCellType().getValue();
            }
        }

        grid.loadData(data);
    }

    private void initMapFromGrid() {
        super.reset();

        //TODO: prevent user from setting more than one start/goal locations.
        MapBuilder builder = new DataMapBuilder(rowCount, columnCount, grid.getData());
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        pathFinder = new HeuristicPathFinder(map);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == buttonRun) {
            registerProgressListener();
            runPathFinder();

        } else if (e.getSource() == buttonReset) {
            init();

        } else if (e.getSource() == buttonSave) {
            initMapFromGrid();

        } else {
            CellType cellType = CellType.fromSymbol(e.getActionCommand().charAt(0));
            grid.setActivationState(cellType.getValue());
        }
    }
}
