import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UnifiedMathSolver extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ScientificCalculatorPanel scientificPanel;
    private MatrixOperationsPanel matrixPanel;
    private PolynomialSolverPanel polynomialPanel;
    private LinearEquationSolverPanel linearEquationPanel;

    public UnifiedMathSolver() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Math Solver Suite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        JMenuBar menuBar = new JMenuBar();
        JMenu appsMenu = new JMenu("Applications");

        String[] appNames = {
            "Scientific Calculator", 
            "Matrix Operations", 
            "Polynomial Solver", 
            "Linear Equation Solver"
        };
        
        for (String appName : appNames) {
            JMenuItem item = new JMenuItem(appName);
            item.addActionListener(e -> switchPanel(appName));
            appsMenu.add(item);
        }
        
        menuBar.add(appsMenu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        scientificPanel = new ScientificCalculatorPanel();
        matrixPanel = new MatrixOperationsPanel();
        polynomialPanel = new PolynomialSolverPanel();
        linearEquationPanel = new LinearEquationSolverPanel();
        
        mainPanel.add(scientificPanel, "Scientific Calculator");
        mainPanel.add(matrixPanel, "Matrix Operations");
        mainPanel.add(polynomialPanel, "Polynomial Solver");
        mainPanel.add(linearEquationPanel, "Linear Equation Solver");
        
        add(mainPanel);
    }

    private void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    class ScientificCalculatorPanel extends JPanel {
        private JTextField inputField1, inputField2;
        private JLabel resultLabel;

        public ScientificCalculatorPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout(5, 5));
            
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            inputField1 = new JTextField();
            inputField2 = new JTextField();
            inputPanel.add(new JLabel("Number 1:"));
            inputPanel.add(inputField1);
            inputPanel.add(new JLabel("Number 2:"));
            inputPanel.add(inputField2);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 5, 5));
            String[] buttons = {
                "Add", "Subtract", "Multiply",
                "Divide", "Square Root", "Power",
                "Exit"
            };

            for (String buttonText : buttons) {
                JButton button = new JButton(buttonText);
                button.addActionListener(new ButtonClickListener());
                buttonPanel.add(button);
            }

            JPanel resultPanel = new JPanel();
            resultLabel = new JLabel("Result: ");
            resultPanel.add(resultLabel);

            add(inputPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.CENTER);
            add(resultPanel, BorderLayout.SOUTH);
        }

        private class ButtonClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                try {
                    double num1 = 0, num2 = 0, result = 0;
                    
                    if (!command.equals("Square Root")) {
                        num1 = Double.parseDouble(inputField1.getText());
                        num2 = Double.parseDouble(inputField2.getText());
                    } else {
                        num1 = Double.parseDouble(inputField1.getText());
                    }

                    switch (command) {
                        case "Add":
                            result = num1 + num2;
                            break;
                        case "Subtract":
                            result = num1 - num2;
                            break;
                        case "Multiply":
                            result = num1 * num2;
                            break;
                        case "Divide":
                            if (num2 == 0) throw new ArithmeticException("Division by zero");
                            result = num1 / num2;
                            break;
                        case "Square Root":
                            if (num1 < 0) throw new ArithmeticException("Negative square root");
                            result = Math.sqrt(num1);
                            break;
                        case "Power":
                            result = Math.pow(num1, num2);
                            break;
                        case "Exit":
                            System.exit(0);
                            break;
                    }

                    resultLabel.setText("Result: " + result);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ScientificCalculatorPanel.this,
                        "Invalid input! Please enter valid numbers.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (ArithmeticException ex) {
                    JOptionPane.showMessageDialog(ScientificCalculatorPanel.this,
                        ex.getMessage(),
                        "Math Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class MatrixOperationsPanel extends JPanel {
        private JTextField rowsField, colsField;
        private JPanel matrixAPanel, matrixBPanel;
        private JButton createButton, addButton, subtractButton, multiplyButton;
        private JButton transposeAButton, transposeBButton, detAButton, detBButton;

        public MatrixOperationsPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Rows:"));
            rowsField = new JTextField(5);
            inputPanel.add(rowsField);
            inputPanel.add(new JLabel("Columns:"));
            colsField = new JTextField(5);
            inputPanel.add(colsField);
            
            createButton = new JButton("Create Matrices");
            inputPanel.add(createButton);
            add(inputPanel, BorderLayout.NORTH);
            
            JPanel matricesContainer = new JPanel(new GridLayout(1, 2));
            matrixAPanel = createMatrixPanel("Matrix A");
            matrixBPanel = createMatrixPanel("Matrix B");
            matricesContainer.add(matrixAPanel);
            matricesContainer.add(matrixBPanel);
            add(matricesContainer, BorderLayout.CENTER);
            
            JPanel operationsPanel = new JPanel(new GridLayout(2, 4));
            addButton = new JButton("Add (A+B)");
            subtractButton = new JButton("Subtract (A-B)");
            multiplyButton = new JButton("Multiply (A×B)");
            transposeAButton = new JButton("Transpose A");
            transposeBButton = new JButton("Transpose B");
            detAButton = new JButton("Determinant A");
            detBButton = new JButton("Determinant B");
            
            operationsPanel.add(addButton);
            operationsPanel.add(subtractButton);
            operationsPanel.add(multiplyButton);
            operationsPanel.add(transposeAButton);
            operationsPanel.add(transposeBButton);
            operationsPanel.add(detAButton);
            operationsPanel.add(detBButton);
            
            add(operationsPanel, BorderLayout.SOUTH);
            
            createButton.addActionListener(e -> createMatrices());
            addButton.addActionListener(e -> performOperation("add"));
            subtractButton.addActionListener(e -> performOperation("subtract"));
            multiplyButton.addActionListener(e -> performOperation("multiply"));
            transposeAButton.addActionListener(e -> transposeMatrix('A'));
            transposeBButton.addActionListener(e -> transposeMatrix('B'));
            detAButton.addActionListener(e -> calculateDeterminant('A'));
            detBButton.addActionListener(e -> calculateDeterminant('B'));
        }

        private JPanel createMatrixPanel(String title) {
            JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder(title));
            return panel;
        }

        private void createMatrices() {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                
                matrixAPanel.removeAll();
                matrixAPanel.setLayout(new GridLayout(rows, cols));
                for (int i = 0; i < rows * cols; i++) {
                    matrixAPanel.add(new JTextField(5));
                }
                
                matrixBPanel.removeAll();
                matrixBPanel.setLayout(new GridLayout(rows, cols));
                for (int i = 0; i < rows * cols; i++) {
                    matrixBPanel.add(new JTextField(5));
                }
                
                matrixAPanel.revalidate();
                matrixAPanel.repaint();
                matrixBPanel.revalidate();
                matrixBPanel.repaint();
            } catch (NumberFormatException ex) {
                showError("Please enter valid dimensions");
            }
        }

        private int[][] getMatrix(JPanel panel, int rows, int cols) {
            int[][] matrix = new int[rows][cols];
            Component[] components = panel.getComponents();
            int index = 0;
            try {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] = Integer.parseInt(((JTextField) components[index++]).getText());
                    }
                }
            } catch (NumberFormatException ex) {
                showError("Please enter valid integers in all matrix cells");
                return null;
            }
            return matrix;
        }

        private void performOperation(String operation) {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                
                int[][] a = getMatrix(matrixAPanel, rows, cols);
                int[][] b = getMatrix(matrixBPanel, rows, cols);
                if (a == null || b == null) return;
                
                int[][] result = null;
                String title = "";
                
                switch (operation) {
                    case "add":
                        result = addMatrices(a, b);
                        title = "Matrix Addition Result";
                        break;
                    case "subtract":
                        result = subtractMatrices(a, b);
                        title = "Matrix Subtraction Result";
                        break;
                    case "multiply":
                        if (cols != Integer.parseInt(rowsField.getText())) {
                            showError("For multiplication, columns of A must equal rows of B");
                            return;
                        }
                        result = multiplyMatrices(a, b);
                        title = "Matrix Multiplication Result";
                        break;
                }
                
                if (result != null) {
                    displayMatrix(result, title);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid matrix dimensions");
            }
        }

        private void transposeMatrix(char matrix) {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                
                int[][] m = matrix == 'A' ? 
                    getMatrix(matrixAPanel, rows, cols) : 
                    getMatrix(matrixBPanel, rows, cols);
                
                if (m == null) return;
                
                int[][] transposed = transposeMatrix(m);
                displayMatrix(transposed, "Transposed Matrix " + matrix);
            } catch (NumberFormatException ex) {
                showError("Invalid matrix dimensions");
            }
        }

        private void calculateDeterminant(char matrix) {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                
                if (rows != cols) {
                    showError("Matrix must be square to calculate determinant");
                    return;
                }
                
                int[][] m = matrix == 'A' ? 
                    getMatrix(matrixAPanel, rows, cols) : 
                    getMatrix(matrixBPanel, rows, cols);
                
                if (m == null) return;
                
                int determinant = calculateDeterminant(m);
                JOptionPane.showMessageDialog(this, "Determinant: " + determinant,
                        "Determinant of Matrix " + matrix, JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                showError("Invalid matrix dimensions");
            }
        }

        private void displayMatrix(int[][] matrix, String title) {
            StringBuilder sb = new StringBuilder();
            for (int[] row : matrix) {
                for (int val : row) {
                    sb.append(val).append("\t");
                }
                sb.append("\n");
            }
            
            JTextArea textArea = new JTextArea(sb.toString(), matrix.length, matrix[0].length * 4);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
        }

        private void showError(String message) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        private int[][] addMatrices(int[][] a, int[][] b) {
            int rows = a.length;
            int cols = a[0].length;
            int[][] result = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = a[i][j] + b[i][j];
                }
            }
            return result;
        }

        private int[][] subtractMatrices(int[][] a, int[][] b) {
            int rows = a.length;
            int cols = a[0].length;
            int[][] result = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = a[i][j] - b[i][j];
                }
            }
            return result;
        }

        private int[][] multiplyMatrices(int[][] a, int[][] b) {
            int rowsA = a.length;
            int colsA = a[0].length;
            int colsB = b[0].length;
            int[][] result = new int[rowsA][colsB];
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    for (int k = 0; k < colsA; k++) {
                        result[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return result;
        }

        private int[][] transposeMatrix(int[][] matrix) {
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[][] result = new int[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[j][i] = matrix[i][j];
                }
            }
            return result;
        }

        private int calculateDeterminant(int[][] matrix) {
            int n = matrix.length;
            if (n == 1) return matrix[0][0];
            if (n == 2) {
                return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            }
            
            int determinant = 0;
            for (int i = 0; i < n; i++) {
                determinant += Math.pow(-1, i) * matrix[0][i] 
                    * calculateDeterminant(getSubmatrix(matrix, 0, i));
            }
            return determinant;
        }

        private int[][] getSubmatrix(int[][] matrix, int row, int col) {
            int n = matrix.length;
            int[][] submatrix = new int[n-1][n-1];
            int r = 0;
            for (int i = 0; i < n; i++) {
                if (i == row) continue;
                int c = 0;
                for (int j = 0; j < n; j++) {
                    if (j == col) continue;
                    submatrix[r][c++] = matrix[i][j];
                }
                r++;
            }
            return submatrix;
        }
    }

    class PolynomialSolverPanel extends JPanel {
        private JTextField degreeField;
        private JPanel coefficientsPanel;
        private JTextArea resultArea;

        public PolynomialSolverPanel() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(600, 500));

            JPanel inputPanel = new JPanel(new FlowLayout());
            inputPanel.add(new JLabel("Enter polynomial degree:"));
            degreeField = new JTextField(5);
            degreeField.setText("2");
            inputPanel.add(degreeField);
            JButton setDegreeButton = new JButton("Set Degree");
            setDegreeButton.addActionListener(this::setDegreeAction);
            inputPanel.add(setDegreeButton);

            add(inputPanel, BorderLayout.NORTH);

            coefficientsPanel = new JPanel();
            coefficientsPanel.setLayout(new BoxLayout(coefficientsPanel, BoxLayout.Y_AXIS));
            JScrollPane coefficientsScrollPane = new JScrollPane(coefficientsPanel);
            add(coefficientsScrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            JButton solveButton = new JButton("Solve Equation");
            solveButton.addActionListener(this::solveAction);
            bottomPanel.add(solveButton, BorderLayout.NORTH);
            
            resultArea = new JTextArea(10, 30);
            resultArea.setEditable(false);
            bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

            add(bottomPanel, BorderLayout.SOUTH);

            createCoefficientFields(2);
        }

        private void setDegreeAction(ActionEvent e) {
            try {
                int degree = Integer.parseInt(degreeField.getText());
                if (degree < 1) throw new NumberFormatException();
                createCoefficientFields(degree);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive integer", 
                                            "Invalid Degree", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void createCoefficientFields(int degree) {
            coefficientsPanel.removeAll();
            for (int i = degree; i >= 0; i--) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new JLabel("Coefficient for x^" + i + ":"));
                JTextField field = new JTextField(10);
                row.add(field);
                coefficientsPanel.add(row);
            }
            coefficientsPanel.revalidate();
            coefficientsPanel.repaint();
        }

        private void solveAction(ActionEvent e) {
            try {
                int degree = Integer.parseInt(degreeField.getText());
				List<Double> coefficientsList = new ArrayList<>();
				
				for (Component comp : coefficientsPanel.getComponents()) {
					JPanel row = (JPanel) comp;
					JTextField field = (JTextField) row.getComponent(1);
					coefficientsList.add(Double.parseDouble(field.getText()));
				}
				
				double[] coefficients = new double[coefficientsList.size()];
				for (int i = 0; i < coefficients.length; i++) {
					coefficients[i] = coefficientsList.get(coefficientsList.size() - 1 - i);
				}
				
				String equation = generatePolynomialEquation(coefficients);
				resultArea.setText("Equation: " + equation + "\n\n");
				
				String[] roots = solvePolynomialEquation(coefficients);
				if (roots.length == 0) {
					resultArea.append("Error: Polynomial degree not supported. Only quadratic and cubic equations are supported.");
				} else {
					resultArea.append("Roots:\n");
					for (String root : roots) {
						resultArea.append(root + "\n");
					}
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter valid numbers for all coefficients",
											"Invalid Input", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error solving equation: " + ex.getMessage(),
											"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		private static String generatePolynomialEquation(double[] coefficients) {
			StringBuilder equation = new StringBuilder();
			for (int i = coefficients.length - 1; i >= 0; i--) {
				if (coefficients[i] != 0) {
					if (equation.length() > 0) {
						equation.append(" + ");
					}
					equation.append(String.format("%.2f", coefficients[i]));
					if (i > 0) {
						equation.append("x^").append(i);
					}
				}
			}
			return equation.toString();
		}

		private static String[] solvePolynomialEquation(double[] coefficients) {
			int degree = coefficients.length - 1;
			if (degree == 2) {
				double a = coefficients[2];
				double b = coefficients[1];
				double c = coefficients[0];
				double discriminant = b * b - 4 * a * c;

				if (discriminant > 0) {
					double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
					double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
					return new String[]{
						String.format("%.4f", root1),
						String.format("%.4f", root2)
					};
				} else if (discriminant == 0) {
					double root = -b / (2 * a);
					return new String[]{ String.format("%.4f", root) };
				} else {
					double realPart = -b / (2 * a);
					double imaginaryPart = Math.sqrt(-discriminant) / (2 * a);
					return new String[]{
						String.format("%.4f + %.4fi", realPart, imaginaryPart),
						String.format("%.4f - %.4fi", realPart, imaginaryPart)
					};
				}
			} else if (degree == 3) {
				double a = coefficients[3];
				double b = coefficients[2];
				double c = coefficients[1];
				double d = coefficients[0];

				double p = c/a - b*b/(3*a*a);
				double q = (2*b*b*b)/(27*a*a*a) - (b*c)/(3*a*a) + d/a;
				double discriminant = q*q/4 + p*p*p/27;

				if (discriminant > 0) {
					double u = Math.cbrt(-q/2 + Math.sqrt(discriminant));
					double v = Math.cbrt(-q/2 - Math.sqrt(discriminant));
					double realRoot = u + v - b/(3*a);
					double realPartComplex = -(u + v)/2 - b/(3*a);
					double imaginaryPart = (u - v) * Math.sqrt(3)/2;
					return new String[]{
						String.format("%.4f", realRoot),
						String.format("%.4f + %.4fi", realPartComplex, imaginaryPart),
						String.format("%.4f - %.4fi", realPartComplex, imaginaryPart)
					};
				} else {
					double r = Math.sqrt(-p*p*p/27);
					double theta = Math.acos(-q/(2*r));
					double root1 = 2*Math.cbrt(r)*Math.cos(theta/3) - b/(3*a);
					double root2 = 2*Math.cbrt(r)*Math.cos((theta + 2*Math.PI)/3) - b/(3*a);
					double root3 = 2*Math.cbrt(r)*Math.cos((theta + 4*Math.PI)/3) - b/(3*a);
					return new String[]{
						String.format("%.4f", root1),
						String.format("%.4f", root2),
						String.format("%.4f", root3)
					};
				}
			} else {
				return new String[0];
			}
		}
	}
	class LinearEquationSolverPanel extends JPanel {
        private JComboBox<Integer> variableCombo;
		private JPanel equationsPanel;
		private JTextArea resultArea;
		private int numVariables = 2;

		public LinearEquationSolverPanel() {
			initComponents();
		}

		private void initComponents() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Linear Equation Solver");
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(600, 400));

			// Top panel for variable selection
			JPanel topPanel = new JPanel(new FlowLayout());
			topPanel.add(new JLabel("Select number of variables:"));
			variableCombo = new JComboBox<>(new Integer[]{2, 3});
			variableCombo.addActionListener(e -> updateEquationFields());
			topPanel.add(variableCombo);

			add(topPanel, BorderLayout.NORTH);

			// Equations input panel
			equationsPanel = new JPanel();
			equationsPanel.setLayout(new GridLayout(0, 1));
			updateEquationFields();
			add(new JScrollPane(equationsPanel), BorderLayout.CENTER);

			// Bottom panel with solve button and results
			JPanel bottomPanel = new JPanel(new BorderLayout());
			JButton solveButton = new JButton("Solve System");
			solveButton.addActionListener(e -> solveEquations());
			bottomPanel.add(solveButton, BorderLayout.NORTH);
			
			resultArea = new JTextArea(8, 30);
			resultArea.setEditable(false);
			bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

			add(bottomPanel, BorderLayout.SOUTH);

			pack();
			setLocationRelativeTo(null);
		}

		private void updateEquationFields() {
			numVariables = (Integer) variableCombo.getSelectedItem();
			equationsPanel.removeAll();
			
			for (int i = 0; i < numVariables; i++) {
				JPanel equationPanel = new JPanel();
				equationPanel.add(new JLabel("Equation " + (i + 1) + ":"));
				
				for (int j = 0; j < numVariables; j++) {
					equationPanel.add(new JLabel("x" + (j + 1) + ":"));
					equationPanel.add(new JTextField(5));
				}
				
				equationPanel.add(new JLabel(" = "));
				equationPanel.add(new JTextField(5));
				equationsPanel.add(equationPanel);
			}
			
			equationsPanel.revalidate();
			equationsPanel.repaint();
			pack();
		}

		private void solveEquations() {
			try {
				double[][] coefficients = new double[numVariables][numVariables];
				double[] constants = new double[numVariables];
				int componentIndex = 0;

				// Collect input values
				for (int i = 0; i < numVariables; i++) {
					Component equationComp = equationsPanel.getComponent(i);
					if (equationComp instanceof JPanel) {
						JPanel equationPanel = (JPanel) equationComp;
						
						for (int j = 0; j < numVariables; j++) {
							JTextField coeffField = (JTextField) equationPanel.getComponent(2 + j * 2);
							coefficients[i][j] = Double.parseDouble(coeffField.getText());
						}
						
						JTextField constField = (JTextField) equationPanel.getComponent(equationPanel.getComponentCount() - 1);
						constants[i] = Double.parseDouble(constField.getText());
					}
				}

				// Solve equations
				double[] solution = solveLinearEquations(coefficients, constants);
				displaySolution(solution);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter valid numbers in all fields",
											 "Input Error", JOptionPane.ERROR_MESSAGE);
			} catch (SingularMatrixException ex) {
				resultArea.setText("No unique solution exists.\nThe system of equations is singular.");
			}
		}

		private double[] solveLinearEquations(double[][] coefficients, double[] constants) throws SingularMatrixException {
			double determinant = calculateDeterminant(coefficients);
			
			if (Math.abs(determinant) < 1e-10) {
				throw new SingularMatrixException();
			}

			double[] solution = new double[numVariables];
			
			for (int i = 0; i < numVariables; i++) {
				double[][] modified = replaceColumn(coefficients, constants, i);
				solution[i] = calculateDeterminant(modified) / determinant;
			}
			
			return solution;
		}

		private double calculateDeterminant(double[][] matrix) {
			if (matrix.length == 2) {
				return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
			} else {
				return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
					 - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
					 + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
			}
		}

		private double[][] replaceColumn(double[][] matrix, double[] column, int colIndex) {
			int n = matrix.length;
			double[][] result = new double[n][n];
			
			for (int i = 0; i < n; i++) {
				System.arraycopy(matrix[i], 0, result[i], 0, n);
				result[i][colIndex] = column[i];
			}
			
			return result;
		}

		private void displaySolution(double[] solution) {
			DecimalFormat df = new DecimalFormat("0.####");
			StringBuilder sb = new StringBuilder("Solution:\n");
			
			for (int i = 0; i < solution.length; i++) {
				sb.append("x").append(i + 1).append(" = ").append(df.format(solution[i])).append("\n");
			}
			
			resultArea.setText(sb.toString());
		}

		private static class SingularMatrixException extends RuntimeException {
			public SingularMatrixException() {
				super("Matrix is singular");
			}
		}
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UnifiedMathSolver().setVisible(true));
    }
}